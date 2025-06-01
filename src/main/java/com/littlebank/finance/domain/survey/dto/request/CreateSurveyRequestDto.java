package com.littlebank.finance.domain.survey.dto.request;

import com.littlebank.finance.domain.survey.domain.Survey;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateSurveyRequestDto {
    @NotNull
    private String question;
    @NotNull
    private String optionA;
    @NotNull
    private String optionB;
    @NotNull
    private String optionC;

    public Survey toSurvey() {
        return Survey.builder()
                .question(question)
                .optionA(optionA)
                .optionB(optionB)
                .optionC(optionC)
                .voteA(0)
                .voteB(0)
                .voteC(0)
                .build();
    }
}
