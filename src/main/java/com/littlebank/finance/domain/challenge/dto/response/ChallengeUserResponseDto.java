package com.littlebank.finance.domain.challenge.dto.response;

import com.littlebank.finance.domain.challenge.domain.ChallengeParticipation;
import com.littlebank.finance.domain.challenge.domain.ChallengeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ChallengeUserResponseDto {
    private Long participationId;
    private Long challengeId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String title;
    private String subject;
    private ChallengeStatus challengeStatus;
    private LocalDateTime startTime;
    private Integer totalStudyTime;
    private Integer reward;
    private Boolean isAccepted;
    private Boolean isRewarded;
    private Integer finishScore;

    public static ChallengeUserResponseDto of(ChallengeParticipation participation) {
        return ChallengeUserResponseDto.builder()
                .participationId(participation.getId())
                .challengeId(participation.getChallenge().getId())
                .startDate(participation.getStartDate())
                .endDate(participation.getEndDate())
                .title(participation.getTitle())
                .subject(participation.getSubject())
                .challengeStatus(participation.getChallengeStatus())
                .startTime(participation.getStartTime())
                .totalStudyTime(participation.getTotalStudyTime())
                .reward(participation.getReward())
                .isAccepted(participation.getIsAccepted())
                .isRewarded(participation.getIsRewarded())
                .finishScore(participation.getFinishScore())
                .build();
    }
}
