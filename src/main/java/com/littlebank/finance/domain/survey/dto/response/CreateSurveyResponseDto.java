package com.littlebank.finance.domain.survey.dto.response;

import com.littlebank.finance.domain.survey.domain.Survey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateSurveyResponseDto {
    private Long quizId;
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private Integer voteA;
    private Integer voteB;
    private Integer voteC;

    public static CreateSurveyResponseDto of(Survey survey) {
        return CreateSurveyResponseDto.builder()
                .quizId(survey.getId())
                .question(survey.getQuestion())
                .optionA(survey.getOptionA())
                .optionB(survey.getOptionB())
                .optionC(survey.getOptionC())
                .voteA(survey.getVoteA())
                .voteB(survey.getVoteB())
                .voteC(survey.getVoteC())
                .build();
    }
}
