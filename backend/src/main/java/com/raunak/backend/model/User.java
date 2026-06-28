package com.raunak.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String userName;

    @Column(unique = true)
    private String email;

    private String leetcodeUsername;
    private boolean leetcodeVerified;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Event> events;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<ContestSession> contestSessions;

    @OneToMany(mappedBy="user")
    @JsonIgnore
    private List<LeetcodeProfile> profiles;

    @OneToOne(mappedBy = "user")
    private SkillProfile skillProfile;
}
