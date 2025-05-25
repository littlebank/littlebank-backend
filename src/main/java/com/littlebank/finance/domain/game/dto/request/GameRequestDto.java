package com.littlebank.finance.domain.game.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.checkerframework.checker.units.qual.N;

@Getter
public class GameRequestDto {
    @NotBlank
    private String question;
    @NotBlank
    private String option_a;
    @NotBlank
    private String option_b;
    @NotBlank
    private Integer vote_a;
    @NotBlank
    private Integer vote_b;
}
