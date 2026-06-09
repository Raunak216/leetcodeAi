package com.raunak.backend.repository;

import com.raunak.backend.model.LeetcodeProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeetcodeProfileRepository extends JpaRepository<LeetcodeProfile,Integer> {
    Optional<LeetcodeProfile> findByUserId(int userId);
}