package com.raunak.backend.service;

import com.raunak.backend.model.Company;
import com.raunak.backend.repository.CompanyRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository){
        this.companyRepository = companyRepository;
    }

    public Company saveCompany(Company company){
        return companyRepository.save(company);
    }

    public List<Company> getAllCompanies(){
        return companyRepository.findAll();
    }
}