package com.raunak.backend.controller;

import com.raunak.backend.dto.EventRequest;
import com.raunak.backend.model.Event;
import com.raunak.backend.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }
    @PostMapping
    public Event createEvent(@Valid @RequestBody EventRequest request){
        return eventService.saveEvent(request);
    }

    @GetMapping
    public List<Event> getEvent(){
        return eventService.getAllEvent();
    }
    @GetMapping("/{id}")
    public Event getEventById(@PathVariable int id){
        return eventService.getEventById(id);
    }

    @GetMapping("/verdict/{verdict}")
    public List<Event> getEventByVerdict(@PathVariable String verdict){
        return eventService.getEventByVerdict(verdict);
    }
    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable int id){
         eventService.deleteEvent(id);
    }

    @GetMapping("/users/{userId}")
    public List<Event> getEventByUserId(@PathVariable int userId){
        return eventService.getEventByUserId(userId);
    }
}
