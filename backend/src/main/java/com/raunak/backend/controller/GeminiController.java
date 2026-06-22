package com.raunak.backend.controller;

import com.raunak.backend.dto.AnalysisResult;
import com.raunak.backend.model.QuestionAttempt;
import com.raunak.backend.service.GeminiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gemini")
public class GeminiController {

    private final GeminiService geminiService;

    public GeminiController(
            GeminiService geminiService
    ) {
        this.geminiService =
                geminiService;
    }

    @GetMapping("/test")
    public String test() {

        QuestionAttempt attempt =
                new QuestionAttempt();

        attempt.setJourneyJson(
                """
                {
                    "steps":[
                        {
                            "eventType":"RUN",
                            "verdict":"Compile Error"
                        },
                        {
                            "eventType":"RUN",
                            "verdict":"Accepted"
                        }
                    ]
                }
                """
        );

        geminiService.analyze(
                attempt
        );
        AnalysisResult result =
                geminiService.analyze(
                        attempt
                );


        return "Check Console";
    }
}