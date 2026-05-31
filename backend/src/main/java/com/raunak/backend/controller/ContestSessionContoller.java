package com.raunak.backend.controller;

import com.raunak.backend.model.ContestSession;
import com.raunak.backend.service.ContestSessionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contestSession")
public class ContestSessionContoller {

    private final ContestSessionService contestSessionService;

    public ContestSessionContoller(ContestSessionService contestSessionService) {
        this.contestSessionService = contestSessionService;
    }

    @GetMapping("/{userId}")
    public ContestSession getContestSession(@PathVariable int userId){
        return contestSessionService.getContestSessionById(userId);
    }

    @PostMapping
    public ContestSession createContestSession(@RequestBody ContestSession session){
        return contestSessionService.saveContestSession(session);
    }

}
