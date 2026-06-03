package com.raunak.backend.repository;

import com.raunak.backend.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event,Integer> {
    List<Event> findByVerdict(String verdict);

    List<Event> findByUserId(int userId);
    long countByVerdict(String verdict);
    long countByContestSessionId(int contestSessionId);

    long countByContestSessionIdAndVerdict(
            int contestSessionId,
            String verdict
    );
    List<Event> findByContestSessionId(int contestSessionId);

    List<Event> findByContestSessionIdOrderByTimestampAsc(int contestSessionId);
}