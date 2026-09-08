package aula.iceibank.service;

import aula.iceibank.entity.EventLog;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class TimelineService {

    private final ObjectMapper objectMapper;

    public TimelineService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<EventLog> unified(Path directory) {
        List<EventLog> events = new ArrayList<>();
        for (int agency = 0; agency < 3; agency++) {
            Path file = directory.resolve("eventos-agencia-" + agency + ".jsonl");
            if (!Files.exists(file)) {
                continue;
            }
            try (var lines = Files.lines(file)) {
                lines.filter(line -> !line.isBlank())
                        .map(this::parse)
                        .forEach(events::add);
            } catch (IOException exception) {
                throw new IllegalStateException("Could not read " + file, exception);
            }
        }
        return events.stream()
                .sorted(Comparator.comparingLong(EventLog::getLamportTimestamp)
                        .thenComparing(EventLog::getAgencyId)
                        .thenComparing(EventLog::getWallClock))
                .toList();
    }

    private EventLog parse(String line) {
        try {
            return objectMapper.readValue(line, EventLog.class);
        } catch (IOException exception) {
            throw new IllegalStateException("Invalid JSONL event", exception);
        }
    }
}
