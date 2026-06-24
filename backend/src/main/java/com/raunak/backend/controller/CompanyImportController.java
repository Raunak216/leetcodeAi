package com.raunak.backend.controller;

import com.raunak.backend.service.CompanyImportService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/import")
public class CompanyImportController {

    private final CompanyImportService companyImportService;

    public CompanyImportController(
            CompanyImportService companyImportService
    ) {
        this.companyImportService =
                companyImportService;
    }

    @PostMapping
    public String importData() {

        companyImportService.importCompanies(
                "C:/Users/rauna/Downloads/LcInterviewQs-master/LcInterviewQs-master"
        );

        return "Imported";
    }
}