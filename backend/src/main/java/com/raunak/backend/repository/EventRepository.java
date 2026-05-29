package com.raunak.backend.repository;

import com.raunak.backend.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event,Integer> {
    List<Event> findByVerdict(String verdict);

}
