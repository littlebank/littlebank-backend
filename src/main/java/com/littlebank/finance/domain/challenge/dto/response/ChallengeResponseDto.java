package com.littlebank.finance.domain.challenge.dto.response;

import com.littlebank.finance.domain.challenge.domain.Challenge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@AllArgsConstructor
public class ChallengeResponseDto {
    private Long id;
    private String title;
    private String category;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalStudyTime;
    private Integer maxParticipants;
    private LocalTime preferredStartTime;
    private Integer preferredReward;

    public static ChallengeResponseDto of(Challenge challenge) {
        return ChallengeResponseDto.builder()
                .id(challenge.getId())
                .title(challenge.getTitle())
                .category(challenge.getCategory().name())
                .startDate(challenge.getStartDate())
                .endDate(challenge.getEndDate())
                .totalStudyTime(challenge.getTotalStudyTime())
                .maxParticipants(challenge.getMaxParticipants())
                .preferredStartTime(challenge.getPreferredStartTime())
                .preferredReward(challenge.getPreferredReward())
                .build();
    }
}
