package com.raunak.backend.service;

import com.raunak.backend.model.QuestionAttempt;
import org.springframework.stereotype.Service;

@Service
public class PromptBuilderService {

    public String buildPrompt(
            QuestionAttempt attempt
    ) {

        return """
        You are an expert competitive programming coach.

        Analyze the coding journey.

        IMPORTANT:
        - Ignore isolated syntax mistakes.
        - Ignore missing semicolons.
        - Ignore typo errors fixed immediately.
        - Only assign negative signals for genuine conceptual weaknesses.
        
                Return ONLY valid JSON.
                
                Use EXACTLY this schema:
                
                {
                  "summary":"string",
                
                  "dsaSignals":{
                      "topic":"EFFICIENT_SOLVE"
                  },
                
                  "engineeringSignals":{
                      "topic":"STRUGGLE"
                  },
                
                  "reasoningSignals":{
                      "topic":"MISTAKE"
                  }
                }
                
                Allowed signals:
                EFFICIENT_SOLVE
                CLEAN_SOLVE
                STRUGGLE
                MISTAKE
                
                Do not use markdown.
                Do not use ```json.
                Do not add explanations.
                

        Journey:
        """
                + attempt.getJourneyJson();
    }
}