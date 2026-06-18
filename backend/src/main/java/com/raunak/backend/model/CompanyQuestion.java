package com.raunak.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CompanyQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String questionSlug;
    private String title;
    private String topic;
    private String difficulty;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}