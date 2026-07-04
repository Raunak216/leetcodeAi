package com.raunak.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private String userName;

    private boolean leetcodeVerified;

    private int totalAttempts;

    private int questionsSolved;

    private int analyzedAttempts;

    private List<TopicMastery> strongTopics;

    private List<TopicMastery> weakTopics;

    private List<String> unexploredTopics;
}