package com.raunak.backend.model;

import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
}
