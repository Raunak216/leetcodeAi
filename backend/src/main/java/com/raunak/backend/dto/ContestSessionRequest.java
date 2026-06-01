package com.raunak.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContestSessionRequest {
    private String contestName;
    private long startTime;
    private long endTime;
    private int userId;
}
