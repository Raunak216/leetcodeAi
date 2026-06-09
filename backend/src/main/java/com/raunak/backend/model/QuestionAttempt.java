package com.raunak.backend.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String topic;
    private String difficulty;
    private int attempts;
    private int timeSpent;
    private boolean accepted;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}