package com.raunak.backend.service;

import com.raunak.backend.dto.EventRequest;
import com.raunak.backend.exception.EventNotFoundException;
import com.raunak.backend.model.Event;
import com.raunak.backend.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private EventRepository eventRepository;
    public EventService(EventRepository eventRepository){
        this.eventRepository=eventRepository;
    }

    public Event saveEvent(EventRequest request){

        Event event=new Event();
        event.setProblemId(request.getProblemId());
        event.setEventType(request.getEventType());
        event.setVerdict(request.getVerdict());
        event.setLanguage(request.getLanguage());
        event.setTimestamp(request.getTimestamp());
        event.setTimeSpent(request.getTimeSpent());
        event.setContestMode(request.isContestMode());
        return eventRepository.save(event);
    }
    public List<Event> getAllEvent(){
        return eventRepository.findAll();
    }
    public Event getEventById(int id){
        return eventRepository.findById(id).orElseThrow(()-> new EventNotFoundException("Event not found with id "+id));
    }
    public List<Event> getEventByVerdict(String verdict){
        return eventRepository.findByVerdict(verdict);
    }
    public void deleteEvent(int id){
         eventRepository.deleteById(id);
    }
}
