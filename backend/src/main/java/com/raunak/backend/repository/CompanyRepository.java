package com.raunak.backend.repository;

import com.raunak.backend.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository
        extends JpaRepository<Company,Integer> {

    Optional<Company> findByName(
            String name
    );
}