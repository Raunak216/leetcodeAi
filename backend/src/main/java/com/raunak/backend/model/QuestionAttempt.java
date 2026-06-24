package com.raunak.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class QuestionAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String questionSlug;

    private String title;

    private String difficulty;

    private String language;

    private Integer runtime;

    private Integer memory;

    private Boolean analysisCompleted = false;

    private Integer analysisRetryCount = 0;

    @Column(columnDefinition = "TEXT")
    private String journeyJson;

    @Column(columnDefinition = "TEXT")
    private String aiResponseJson;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}