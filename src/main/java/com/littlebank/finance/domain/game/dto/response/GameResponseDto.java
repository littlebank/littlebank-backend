package com.littlebank.finance.domain.game.dto.response;

import com.littlebank.finance.domain.game.domain.Game;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GameResponseDto {
    private Long id;
    private String question;
    private String option_a;
    private String option_b;
    private Integer vote_a;
    private Integer vote_b;

    public static GameResponseDto of(Game game) {
        return GameResponseDto.builder()
                .id(game.getId())
                .question(game.getQuestion())
                .option_a(game.getOption_a())
                .option_b(game.getOption_b())
                .vote_a(game.getVote_a())
                .vote_b(game.getVote_b())
                .build();
    }
}
