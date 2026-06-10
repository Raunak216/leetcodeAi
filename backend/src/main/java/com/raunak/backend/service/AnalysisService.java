package com.raunak.backend.service;

import com.raunak.backend.dto.TopicAnalysisResponse;
import com.raunak.backend.dto.TopicCoverageResponse;
import com.raunak.backend.dto.WeaknessResponse;
import com.raunak.backend.model.QuestionAttempt;
import com.raunak.backend.repository.QuestionAttemptRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalysisService {

    private final QuestionAttemptRepository repository;

    public AnalysisService(QuestionAttemptRepository repository) {
        this.repository = repository;
    }

    public List<TopicAnalysisResponse> getTopicAnalysis(int userId){
        List<QuestionAttempt> attempts = repository.findByUserId(userId);
        Map<String,Integer> totalAttempts = new HashMap<>();
        Map<String,Integer> questionCount = new HashMap<>();

        for(QuestionAttempt attempt : attempts){
            String topic = attempt.getTopic();
            totalAttempts.put(topic, totalAttempts.getOrDefault(topic, 0) + attempt.getAttempts());

            questionCount.put(topic, questionCount.getOrDefault(topic, 0) + 1);
        }

        List<TopicAnalysisResponse> result = new ArrayList<>();

        for(String topic : totalAttempts.keySet()){
            double averageAttempts = (double) totalAttempts.get(topic)/ questionCount.get(topic);
            result.add(new TopicAnalysisResponse(
                            topic,
                            questionCount.get(topic),
                            averageAttempts
                    ));
        }

        return result;
    }


    public List<WeaknessResponse> getWeaknesses(int userId){
        List<TopicAnalysisResponse> topics = getTopicAnalysis(userId);
        List<WeaknessResponse> result = new ArrayList<>();
        for(TopicAnalysisResponse topic : topics){
            if(topic.getAverageAttempts() >= 4){
                result.add(new WeaknessResponse(topic.getTopic(), "Average attempts is " + topic.getAverageAttempts()));
            }
        }
        return  result;
    }

    public List<TopicCoverageResponse> getTopicCoverage(int userId){

        List<QuestionAttempt> attempts = repository.findByUserId(userId);
        Map<String,Integer> count = new HashMap<>();

        for(QuestionAttempt attempt : attempts){
            String topic = attempt.getTopic();
            count.put(topic, count.getOrDefault(topic,0) + 1);
        }
        List<TopicCoverageResponse> result = new ArrayList<>();
        for(String topic : count.keySet()){
            result.add(new TopicCoverageResponse(topic, count.get(topic)));
        }
        return result;
    }
}