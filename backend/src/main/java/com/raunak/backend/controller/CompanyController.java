package com.raunak.backend.controller;

import com.raunak.backend.model.Company;
import com.raunak.backend.service.CompanyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService){
        this.companyService = companyService;
    }

    @PostMapping
    public Company createCompany(@RequestBody Company company){
        return companyService.saveCompany(company);
    }

    @GetMapping
    public List<Company> getCompanies(){
        return companyService.getAllCompanies();
    }
}