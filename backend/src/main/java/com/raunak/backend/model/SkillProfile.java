package com.raunak.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SkillProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "TEXT")
    private String dsa;

    @Column(columnDefinition = "TEXT")
    private String engineering;

    @Column(columnDefinition = "TEXT")
    private String reasoning;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}