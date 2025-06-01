package com.littlebank.finance.domain.quiz.dto.response;

import com.littlebank.finance.domain.quiz.domain.Quiz;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateQuizResponseDto {
    private Long quizId;
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private Integer voteA;
    private Integer voteB;
    private Integer voteC;

    public static CreateQuizResponseDto of(Quiz quiz) {
        return CreateQuizResponseDto.builder()
                .quizId(quiz.getId())
                .question(quiz.getQuestion())
                .optionA(quiz.getOptionA())
                .optionB(quiz.getOptionB())
                .optionC(quiz.getOptionC())
                .voteA(quiz.getVoteA())
                .voteB(quiz.getVoteB())
                .voteC(quiz.getVoteC())
                .build();
    }
}
