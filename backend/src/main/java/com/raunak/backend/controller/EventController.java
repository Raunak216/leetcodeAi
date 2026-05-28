package com.raunak.backend.controller;

import com.raunak.backend.model.Event;
import com.raunak.backend.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;
    @PostMapping
    public Event createEvent(@RequestBody Event event){
        return eventService.saveEvent((event));
    }
}
