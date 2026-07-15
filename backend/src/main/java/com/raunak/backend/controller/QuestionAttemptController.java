package com.raunak.backend.controller;

import com.raunak.backend.dto.QuestionAttemptRequest;
import com.raunak.backend.model.QuestionAttempt;
import com.raunak.backend.service.QuestionAttemptService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/attempts")
public class QuestionAttemptController {

    private final QuestionAttemptService questionAttemptService;

    public QuestionAttemptController(QuestionAttemptService questionAttemptService) {

        this.questionAttemptService = questionAttemptService;
    }
    @PostMapping
    public QuestionAttempt createAttempt(@Valid @RequestBody QuestionAttemptRequest request) {
        return questionAttemptService.saveAttempt(request);
    }
    @GetMapping("/user/{userId}")
    public List<QuestionAttempt> getAttemptsByUser(@PathVariable int userId) {
        return questionAttemptService.getAttemptsByUser(userId);
    }
    @GetMapping("/{attemptId}")
    public QuestionAttempt getAttempt(
            @PathVariable
            int attemptId
    )
    {
        return questionAttemptService
                .getAttempt(
                        attemptId
                );
    }
}