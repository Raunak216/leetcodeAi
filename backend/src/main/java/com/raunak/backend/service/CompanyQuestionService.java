package com.raunak.backend.service;

import com.raunak.backend.dto.CompanyQuestionRequest;
import com.raunak.backend.model.Company;
import com.raunak.backend.model.CompanyQuestion;
import com.raunak.backend.repository.CompanyQuestionRepository;
import com.raunak.backend.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyQuestionService {

    private final CompanyQuestionRepository companyQuestionRepository;
    private final CompanyRepository companyRepository;

    public CompanyQuestionService(CompanyQuestionRepository companyQuestionRepository, CompanyRepository companyRepository) {
        this.companyQuestionRepository = companyQuestionRepository;
        this.companyRepository = companyRepository;
    }

    public CompanyQuestion saveQuestion(CompanyQuestionRequest request) {

        Company company = companyRepository.findById(request.getCompanyId()).orElseThrow(
                () -> new RuntimeException("Company not found"));

        CompanyQuestion question = new CompanyQuestion();

        question.setTitle(request.getTitle());

        question.setCompany(company);

        return companyQuestionRepository.save(question);
    }

    public List<CompanyQuestion> getQuestionsByCompany(int companyId){
        return companyQuestionRepository.findByCompanyId(companyId);
    }
}