package com.raunak.backend.dto;

import com.raunak.backend.model.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeetcodeProfileRequest {
    @NotBlank
    private String leetcodeUsername;
    private int easySolved;
    private int mediumSolved;
    private int hardSolved;
    private int contestRating;
    private LocalDateTime lastSyncedAt;
    @NotNull
    private int userId;
}
