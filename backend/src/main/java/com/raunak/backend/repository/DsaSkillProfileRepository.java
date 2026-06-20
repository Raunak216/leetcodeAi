package com.raunak.backend.repository;

import com.raunak.backend.model.DsaSkillProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DsaSkillProfileRepository
        extends JpaRepository<DsaSkillProfile,Integer> {

    Optional<DsaSkillProfile> findByUserId(int userId);
}