package com.raunak.backend.repository;

import com.raunak.backend.model.LeetcodeProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeetcodeProfileRepository extends JpaRepository<LeetcodeProfile,Integer> {
    LeetcodeProfile findByUserId(int userId);
}