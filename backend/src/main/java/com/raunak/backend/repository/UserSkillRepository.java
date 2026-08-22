package com.raunak.backend.repository;

import com.raunak.backend.model.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserSkillRepository
        extends JpaRepository<UserSkill, Integer> {

    List<UserSkill> findByUserId(int userId);

    Optional<UserSkill> findByUserIdAndSkill(
            int userId,
            String skill
    );
}