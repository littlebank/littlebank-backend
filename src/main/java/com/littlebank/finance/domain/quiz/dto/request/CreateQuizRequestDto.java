package com.littlebank.finance.domain.quiz.dto.request;

import com.littlebank.finance.domain.quiz.domain.Quiz;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateQuizRequestDto {
    @NotNull
    private String question;
    @NotNull
    private String optionA;
    @NotNull
    private String optionB;
    @NotNull
    private String optionC;

    public Quiz toQuiz() {
        return Quiz.builder()
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
