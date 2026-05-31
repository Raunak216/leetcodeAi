package com.raunak.backend.repository;

import com.raunak.backend.model.ContestSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContestSessionRepository extends JpaRepository<ContestSession,Integer> {
}
