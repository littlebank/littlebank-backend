package com.littlebank.finance.domain.challenge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChallengeDetailResponseDto {
    private Long id;
    private String title;
    private String category;
    private String description;
    private String startDate;
    private String endDate;
    private Integer totalStudyTime;
    private int currentParticipants;
    private Integer maxParticipants;
    private String preferredStartTime;
    private Integer preferredReward;
}
