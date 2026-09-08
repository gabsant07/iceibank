package aula.iceibank.controller;

import aula.iceibank.config.BankProperties;
import aula.iceibank.entity.EventLog;
import aula.iceibank.service.TimelineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/timeline")
public class TimelineController {

    private final TimelineService timelineService;
    private final BankProperties properties;

    public TimelineController(TimelineService timelineService, BankProperties properties) {
        this.timelineService = timelineService;
        this.properties = properties;
    }

    @GetMapping
    public ResponseEntity<List<EventLog>> unified() {
        return ResponseEntity.ok(timelineService.unified(Path.of(properties.getEventDirectory())));
    }
}
