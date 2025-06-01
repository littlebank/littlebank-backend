package com.littlebank.finance.domain.survey.dto.response;

import com.littlebank.finance.domain.game.dto.response.GameVoteResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class SurveyVoteResponseDto {
    private Long quizId;
    private String choice;
    private Integer voteA;
    private Integer voteB;
    private Integer voteC;
    private double percentA;
    private double percentB;
    private double percentC;

    public static SurveyVoteResponseDto of(Long quizId, String choice, Integer voteA, Integer voteB, Integer voteC) {
        int totalVote = voteA + voteB + voteC;
        double percentA = totalVote == 0? 0 : ((double) voteA / totalVote) * 100;
        double percentB = totalVote == 0? 0 : ((double) voteB / totalVote) * 100;
        double percentC = totalVote == 0? 0 : ((double) voteC / totalVote) * 100;
        return SurveyVoteResponseDto.builder()
                .quizId(quizId)
                .choice(choice)
                .voteA(voteA)
                .voteB(voteB)
                .voteC(voteC)
                .percentA(percentA)
                .percentB(percentB)
                .percentC(percentC)
                .build();

    }
}
