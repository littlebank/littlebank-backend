package com.littlebank.finance.domain.challenge.dto.response;

import com.littlebank.finance.domain.challenge.domain.ChallengeParticipation;
import com.littlebank.finance.domain.family.domain.FamilyMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChallengeFinishScoreResponseDto {
    private Long participationId;
    private Integer score;

    public static ChallengeFinishScoreResponseDto of(ChallengeParticipation participation, Integer score) {
        return ChallengeFinishScoreResponseDto.builder()
                .participationId(participation.getId())
                .score(score)
                .build();
    }
}
