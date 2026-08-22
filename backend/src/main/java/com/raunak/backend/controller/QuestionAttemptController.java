package com.raunak.backend.controller;

import com.raunak.backend.dto.AiInsightsResponse;
import com.raunak.backend.dto.QuestionAttemptRequest;
import com.raunak.backend.model.QuestionAttempt;
import com.raunak.backend.security.AuthUser;
import com.raunak.backend.service.QuestionAttemptService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attempts")
public class QuestionAttemptController {

    private final QuestionAttemptService questionAttemptService;

    public QuestionAttemptController(
            QuestionAttemptService questionAttemptService
    ) {
        this.questionAttemptService =
                questionAttemptService;
    }

    @PostMapping
    public QuestionAttempt createAttempt(
            @Valid
            @RequestBody
            QuestionAttemptRequest request
    ) {

        return questionAttemptService
                .saveAttempt(request);
    }

    @GetMapping("/me")
    public List<QuestionAttempt> getMyAttempts(
            Authentication authentication
    ) {

        AuthUser authUser =
                (AuthUser)
                        authentication.getPrincipal();

        return questionAttemptService
                .getAttemptsByUser(
                        authUser.getUserId()
                );
    }

    @GetMapping("/{attemptId}")
    public QuestionAttempt getAttempt(
            @PathVariable
            int attemptId,
            Authentication authentication
    ) {

        AuthUser authUser =
                (AuthUser)
                        authentication.getPrincipal();

        return questionAttemptService
                .getAttempt(
                        attemptId,
                        authUser.getUserId()
                );
    }

    @PostMapping("/{attemptId}/insights")
    public AiInsightsResponse generateInsights(
            @PathVariable
            int attemptId,
            Authentication authentication
    ) {

        AuthUser authUser =
                (AuthUser)
                        authentication.getPrincipal();

        return questionAttemptService
                .generateInsights(
                        attemptId,
                        authUser.getUserId()
                );
    }
}