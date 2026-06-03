package com.raunak.backend.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class TimelineEvent {
    private String problemId;
    private String verdict;
    private long timestamp;
}
