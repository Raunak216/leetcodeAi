package com.raunak.backend.controller;

import com.raunak.backend.dto.CompanyQuestionRequest;
import com.raunak.backend.model.CompanyQuestion;
import com.raunak.backend.service.CompanyQuestionService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/company-questions")
public class CompanyQuestionController {

    private final CompanyQuestionService companyQuestionService;

    public CompanyQuestionController(CompanyQuestionService companyQuestionService) {
        this.companyQuestionService = companyQuestionService;
    }

    @PostMapping
    public CompanyQuestion createQuestion(@RequestBody CompanyQuestionRequest request) {
        return companyQuestionService.saveQuestion(request);
    }

    @GetMapping("/company/{companyId}")
    public List<CompanyQuestion> getQuestionsByCompany(@PathVariable int companyId) {
        return companyQuestionService.getQuestionsByCompany(companyId);
    }
}