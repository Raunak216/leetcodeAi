package com.raunak.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="contest_sessions")
@Getter
@Setter
@NoArgsConstructor
public class ContestSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String ContestName;
    private long startTime;
    private long endTime;

    @OneToMany(mappedBy = "contestSession")
    @JsonIgnore
    private List<Event> events;
}
