package com.raunak.backend.service;

import com.raunak.backend.model.Company;
import com.raunak.backend.model.CompanyQuestion;
import com.raunak.backend.repository.CompanyQuestionRepository;
import com.raunak.backend.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

@Service
public class CompanyImportService {

    private final CompanyRepository companyRepository;

    private final CompanyQuestionRepository companyQuestionRepository;

    public CompanyImportService(
            CompanyRepository companyRepository,
            CompanyQuestionRepository companyQuestionRepository
    ) {
        this.companyRepository =
                companyRepository;

        this.companyQuestionRepository =
                companyQuestionRepository;
    }

    public void importCompanies(
            String rootFolderPath
    ) {

        try {

            File rootFolder =
                    new File(
                            rootFolderPath
                    );

            File[] companyFolders =
                    rootFolder.listFiles(
                            File::isDirectory
                    );

            if (
                    companyFolders == null
            ) {
                return;
            }

            for (
                    File companyFolder :
                    companyFolders
            ) {

                String companyName =
                        companyFolder.getName();

                Company company =
                        companyRepository
                                .findByName(
                                        companyName
                                )
                                .orElseGet(() -> {

                                    Company c =
                                            new Company();

                                    c.setName(
                                            companyName
                                    );

                                    return companyRepository
                                            .save(c);
                                });

                File csvFile =
                        new File(
                                companyFolder,
                                "all.csv"
                        );

                if (
                        !csvFile.exists()
                ) {
                    continue;
                }

                BufferedReader reader =
                        new BufferedReader(
                                new FileReader(
                                        csvFile
                                )
                        );

                reader.readLine();

                String line;

                while (
                        (
                                line =
                                        reader.readLine()
                        ) != null
                ) {

                    String[] parts =
                            line.split(",");

                    if (
                            parts.length < 3
                    ) {
                        continue;
                    }

                    String title =
                            parts[2]
                                    .trim();

                    if(
                            companyQuestionRepository
                                    .existsByTitleAndCompanyId(
                                            title,
                                            company.getId()
                                    )
                    ){
                        continue;
                    }
                    CompanyQuestion question =
                            new CompanyQuestion();


                    question.setTitle(
                            title
                    );

                    question.setCompany(
                            company
                    );

                    companyQuestionRepository
                            .save(
                                    question
                            );
                }

                reader.close();
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    e
            );
        }
    }
}