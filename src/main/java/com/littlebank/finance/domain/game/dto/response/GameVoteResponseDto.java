package com.littlebank.finance.domain.game.dto.response;

import com.littlebank.finance.domain.game.domain.GameChoice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GameVoteResponseDto {
    private Long gameId;
    private GameChoice choice;
    private int voteCountA;
    private int voteCountB;
    private double percentageA;
    private double percentageB;

    public static GameVoteResponseDto of(Long gameId, GameChoice choice, int voteA, int voteB) {
        int total = voteA + voteB;
        double percentA = total == 0? 0: ((double) voteA / total) * 100;
        double percentB = total == 0? 0: ((double) voteB / total) * 100;

        return GameVoteResponseDto.builder()
                .gameId(gameId)
                .choice(choice)
                .voteCountA(voteA)
                .voteCountB(voteB)
                .percentageA(Math.round(percentA * 10) / 10.0)
                .percentageB(Math.round(percentB * 10) / 10.0)
                .build();
    }
}
