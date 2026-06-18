package com.raunak.backend.repository;

import com.raunak.backend.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository
        extends JpaRepository<Company,Integer> {
}