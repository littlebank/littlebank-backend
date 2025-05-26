package com.littlebank.finance.domain.game.dto.request;

import com.littlebank.finance.domain.game.domain.GameChoice;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GameVoteRequestDto {
    private GameChoice choice;
}
