package com.littlebank.finance.domain.challenge.dto.response;

import com.littlebank.finance.domain.challenge.domain.ChallengeParticipation;
import com.littlebank.finance.domain.challenge.domain.ChallengeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@AllArgsConstructor
public class ChallengeUserResponseDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private String subject;
    private ChallengeStatus challengeStatus;
    private LocalTime startTime;
    private Integer totalStudyTime;
    private Integer reward;

    public static ChallengeUserResponseDto of(ChallengeParticipation participation) {
        return ChallengeUserResponseDto.builder()
                .startDate(participation.getStartDate())
                .endDate(participation.getEndDate())
                .subject(participation.getSubject())
                .challengeStatus(participation.getChallengeStatus())
                .startTime(participation.getStartTime())
                .totalStudyTime(participation.getTotalStudyTime())
                .reward(participation.getReward())

                .build();
    }
}
