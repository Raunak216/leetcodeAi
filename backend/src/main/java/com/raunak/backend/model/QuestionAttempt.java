package com.raunak.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private Integer compileErrors = 0;

    private Integer logicFailures = 0;

    private Boolean accepted = false;

    private Boolean analysisCompleted = false;

    private Integer analysisRetryCount = 0;

    @Column(columnDefinition = "TEXT")
    private String journeyJson;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}