package com.raunak.backend.controller;

import com.raunak.backend.dto.AnalyticsResponse;
import com.raunak.backend.service.EventService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final EventService eventService;

    public AnalyticsController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/contest-session/{id}")
    public AnalyticsResponse getSessionAnalytics(@PathVariable int id) {

        return eventService.getSessionAnalytics(id);
    }
}