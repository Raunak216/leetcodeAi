package com.raunak.backend.repository;

import com.raunak.backend.model.CompanyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyQuestionRepository extends JpaRepository<CompanyQuestion,Integer> {
    List<CompanyQuestion> findByCompanyId(int companyId);
    boolean existsByTitleAndCompanyId(String title, int companyId);
}