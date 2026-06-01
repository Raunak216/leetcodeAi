package com.raunak.backend.service;

import com.raunak.backend.dto.ContestSessionRequest;
import com.raunak.backend.model.ContestSession;
import com.raunak.backend.model.User;
import com.raunak.backend.repository.ContestSessionRepository;
import com.raunak.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContestSessionService {
    private final ContestSessionRepository contestSessionRepository;
    private final UserRepository userRepository;

    public ContestSessionService(
            ContestSessionRepository contestSessionRepository, UserRepository userRepository) {
        this.contestSessionRepository = contestSessionRepository;
        this.userRepository=userRepository;
    }
    public ContestSession getContestSessionById(int id){
        return contestSessionRepository.findById(id).orElseThrow(()->new RuntimeException("User not found"));
    }
    public ContestSession saveContestSession(
            ContestSessionRequest request){

        User user = userRepository.findById(
                request.getUserId()
        ).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        ContestSession session = new ContestSession();

        session.setContestName(request.getContestName());
        session.setStartTime(request.getStartTime());
        session.setEndTime(request.getEndTime());
        session.setUser(user);

        return contestSessionRepository.save(session);
    }
    public List<ContestSession> getAllContestSessions(){
        return contestSessionRepository.findAll();
    }
}
