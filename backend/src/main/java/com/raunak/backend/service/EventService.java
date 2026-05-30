package com.raunak.backend.service;

import com.raunak.backend.dto.EventRequest;
import com.raunak.backend.exception.EventNotFoundException;
import com.raunak.backend.model.Event;
import com.raunak.backend.model.User;
import com.raunak.backend.repository.EventRepository;
import com.raunak.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private EventRepository eventRepository;
    private UserRepository userRepository;
    public EventService(EventRepository eventRepository,UserRepository userRepository){
        this.eventRepository=eventRepository;
        this.userRepository=userRepository;
    }


    public Event saveEvent(EventRequest request){

        Event event=new Event();
        User user=userRepository.findById(request.getUserId()).orElseThrow(()->new RuntimeException("User not found"));
        event.setUser(user);
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

    public List<Event> getEventByUserId(int userId){
        return eventRepository.findByUserId(userId);
    }
}
