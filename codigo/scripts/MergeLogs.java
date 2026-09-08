import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MergeLogs {

    private static final Pattern AGENCY = Pattern.compile("\\\"agencyId\\\"\\s*:\\s*(\\d+)");
    private static final Pattern TIMESTAMP = Pattern.compile("\\\"lamportTimestamp\\\"\\s*:\\s*(\\d+)");
    private static final Pattern TYPE = Pattern.compile("\\\"type\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"");

    public static void main(String[] args) throws IOException {
        Path directory = Path.of(args.length == 0 ? "data" : args[0]);
        List<LogLine> events = new ArrayList<>();
        for (int agency = 0; agency < 3; agency++) {
            Path file = directory.resolve("eventos-agencia-" + agency + ".jsonl");
            if (Files.exists(file)) {
                for (String line : Files.readAllLines(file)) {
                    if (!line.isBlank()) {
                        events.add(parse(line));
                    }
                }
            }
        }
        events.stream()
                .sorted(Comparator.comparingLong(LogLine::timestamp).thenComparingInt(LogLine::agency))
                .forEach(event -> System.out.printf("Lamport %d | Agency %d | %s%n",
                        event.timestamp(), event.agency(), event.type()));
    }

    private static LogLine parse(String json) {
        return new LogLine(number(AGENCY, json), number(TIMESTAMP, json), text(TYPE, json));
    }

    private static int number(Pattern pattern, String value) {
        Matcher matcher = pattern.matcher(value);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Invalid event: " + value);
        }
        return Integer.parseInt(matcher.group(1));
    }

    private static String text(Pattern pattern, String value) {
        Matcher matcher = pattern.matcher(value);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Invalid event: " + value);
        }
        return matcher.group(1);
    }

    private record LogLine(int agency, long timestamp, String type) {
    }
}
