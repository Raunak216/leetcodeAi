package com.raunak.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {
    @NotBlank
    private String problemId;
    @NotBlank
    private String eventType;
    @NotBlank
    private String verdict;
    private String language;
    private long timestamp;
    private int timeSpent;
    private boolean contestMode;
    private int userId;
}
