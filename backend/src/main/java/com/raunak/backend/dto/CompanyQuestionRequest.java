package com.raunak.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyQuestionRequest {

    private String questionSlug;
    private String title;
    private String topic;
    private String difficulty;

    private int companyId;
}