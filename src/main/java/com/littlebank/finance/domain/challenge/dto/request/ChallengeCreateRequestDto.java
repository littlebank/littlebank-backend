package com.littlebank.finance.domain.challenge.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeCreateRequestDto {
    private String title;
    private String category;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalStudyTime;
    private Integer maxParticipants;
    private LocalTime preferredStartTime;
    private Integer preferredReward;
}
