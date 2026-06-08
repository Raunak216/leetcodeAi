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
public class LeetcodeProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String leetcodeUsername;

    private int easySolved;
    private int mediumSolved;
    private int hardSolved;

    private int contestRating;

    private LocalDateTime lastSyncedAt;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}