package com.raunak.backend.repository;


import com.raunak.backend.model.Skills;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillsRepository
        extends JpaRepository<Skills,Integer> {

    Optional<Skills>
    findByUserId(int userId);
}