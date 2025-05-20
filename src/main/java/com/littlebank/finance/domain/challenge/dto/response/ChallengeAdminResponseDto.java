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
public class ChallengeAdminResponseDto {
    private Long id;
    private String title;
    private String description;
    private String category;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalStudyTime;
    private Integer currentParticipants;
    private Integer totalParticipants;
    private int viewCount;

    public static ChallengeAdminResponseDto of(Challenge challenge, int currentActiveParticipants) {
        return ChallengeAdminResponseDto.builder()
                .id(challenge.getId())
                .title(challenge.getTitle())
                .description(challenge.getDescription())
                .category(challenge.getCategory().name())
                .startDate(challenge.getStartDate())
                .endDate(challenge.getEndDate())
                .totalStudyTime(challenge.getTotalStudyTime())
                .currentParticipants(currentActiveParticipants)
                .totalParticipants(challenge.getTotalParticipants())
                .viewCount(challenge.getViewCount())
                .build();
    }
}
