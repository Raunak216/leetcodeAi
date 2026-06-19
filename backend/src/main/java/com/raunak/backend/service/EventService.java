package com.raunak.backend.service;

import com.raunak.backend.dto.EventRequest;
import com.raunak.backend.dto.ProblemAnalytics;
import com.raunak.backend.dto.TimelineEvent;
import com.raunak.backend.exception.EventNotFoundException;
import com.raunak.backend.model.ContestSession;
import com.raunak.backend.model.Event;
import com.raunak.backend.model.User;
import com.raunak.backend.repository.ContestSessionRepository;
import com.raunak.backend.repository.EventRepository;
import com.raunak.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class EventService {

    private EventRepository eventRepository;
    private UserRepository userRepository;
    private final ContestSessionRepository contestSessionRepository;
    public EventService(EventRepository eventRepository,UserRepository userRepository,ContestSessionRepository contestSessionRepository){
        this.eventRepository=eventRepository;
        this.userRepository=userRepository;
        this.contestSessionRepository=contestSessionRepository;
    }


    public Event saveEvent(EventRequest request){

        Event event=new Event();
        User user=userRepository.findById(request.getUserId()).orElseThrow(()->new RuntimeException("User not found"));
        event.setUser(user);

        ContestSession contestSession = contestSessionRepository.findById(request.getContestSessionId()).orElseThrow(() -> new RuntimeException("Contest Session not found"));
        event.setContestSession(contestSession);
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

    public List<ProblemAnalytics> getProblemAttempts(int sessionId){
        List<Event> events = eventRepository.findByContestSessionId(sessionId);
        Map<String,Integer> attempts = new HashMap<>();

        for(Event event : events){
            String problemId = event.getProblemId();
            attempts.put(problemId,attempts.getOrDefault(problemId,0)+1);
        }
        List<ProblemAnalytics> result = new ArrayList<>();

        for(String problemId :attempts.keySet()){
            result.add(new ProblemAnalytics(problemId, attempts.get(problemId)));
        }
        return result;
    }

    public List<TimelineEvent> getSessionTimeline(int sessionId){

        List<Event> events = eventRepository.findByContestSessionIdOrderByTimestampAsc(sessionId);

        List<TimelineEvent> result = new ArrayList<>();

        for(Event event : events){
            result.add(new TimelineEvent(event.getProblemId(), event.getVerdict(), event.getTimestamp()));
        }

        return result;
    }
}
