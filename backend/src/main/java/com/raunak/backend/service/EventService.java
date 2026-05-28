package com.raunak.backend.service;

import com.raunak.backend.model.Event;
import com.raunak.backend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public Event saveEvent(Event event){
        return eventRepository.save(event);
    }
}
