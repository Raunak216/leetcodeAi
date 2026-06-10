package com.raunak.backend.controller;

import com.raunak.backend.dto.TopicAnalysisResponse;
import com.raunak.backend.dto.TopicCoverageResponse;
import com.raunak.backend.dto.WeaknessResponse;
import com.raunak.backend.service.AnalysisService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    private final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @GetMapping("/topics/{userId}")
    public List<TopicAnalysisResponse> getTopicAnalysis(@PathVariable int userId){

        return analysisService.getTopicAnalysis(userId);
    }
    @GetMapping("/weaknesses/{userId}")
    public List<WeaknessResponse> getWeaknesses(@PathVariable int userId){
        return analysisService.getWeaknesses(userId);
    }

    @GetMapping("/coverage/{userId}")
    public List<TopicCoverageResponse> getCoverage(@PathVariable int userId){
        return analysisService
                .getTopicCoverage(userId);
    }
}