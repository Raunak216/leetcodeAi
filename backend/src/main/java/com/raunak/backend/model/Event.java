package com.raunak.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String problemId;
    private String  eventType;
    private String verdict;
    private String language;
    private long timestamp;
    private int timeSpent;
    private boolean contestMode;
}
