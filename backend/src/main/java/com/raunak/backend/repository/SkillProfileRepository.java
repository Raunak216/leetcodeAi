package com.raunak.backend.repository;

import com.raunak.backend.model.SkillProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillProfileRepository
        extends JpaRepository<SkillProfile,Integer> {

    Optional<SkillProfile> findByUserId(int userId);
}