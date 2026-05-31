package com.raunak.backend.service;

import com.raunak.backend.model.ContestSession;
import com.raunak.backend.repository.ContestSessionRepository;
import org.springframework.stereotype.Service;

@Service
public class ContestSessionService {
    private final ContestSessionRepository contestSessionRepository;

    public ContestSessionService(
            ContestSessionRepository contestSessionRepository) {
        this.contestSessionRepository = contestSessionRepository;
    }
    public ContestSession getContestSessionById(int id){
        return contestSessionRepository.findById(id).orElseThrow(()->new RuntimeException("User not found"));
    }
    public ContestSession saveContestSession(ContestSession session){
        return contestSessionRepository.save(session);
    }
}
