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
public class Skills {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(
            name = "user_id",
            unique = true
    )
    private User user;

    @Column(columnDefinition = "TEXT")
    private String skillsJson;

}