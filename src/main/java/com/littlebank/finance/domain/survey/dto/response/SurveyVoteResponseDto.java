package com.littlebank.finance.domain.survey.dto.response;

import com.littlebank.finance.domain.survey.domain.Survey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class SurveyVoteResponseDto {
    private Long surveyId;
    private String choice;
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private Integer voteA;
    private Integer voteB;
    private Integer voteC;
    private double percentA;
    private double percentB;
    private double percentC;


    public static SurveyVoteResponseDto ofWithResult(Survey survey, String choice) {
        int voteA = survey.getVoteA();
        int voteB = survey.getVoteB();
        int voteC = survey.getVoteC();
        int totalVote = voteA + voteB + voteC;
        double percentA = totalVote == 0? 0 : ((double) voteA / totalVote) * 100;
        double percentB = totalVote == 0? 0 : ((double) voteB / totalVote) * 100;
        double percentC = totalVote == 0? 0 : ((double) voteC / totalVote) * 100;
        return SurveyVoteResponseDto.builder()
                .surveyId(survey.getId())
                .question(survey.getQuestion())
                .optionA(survey.getOptionA())
                .optionB(survey.getOptionB())
                .optionC(survey.getOptionC())
                .choice(choice)
                .voteA(voteA)
                .voteB(voteB)
                .voteC(voteC)
                .percentA(Math.round(percentA * 10) / 10.0)
                .percentB(Math.round(percentB * 10) / 10.0)
                .percentC(Math.round(percentC * 10) / 10.0)
                .build();
    }

    public static SurveyVoteResponseDto ofWithoutResult(Survey survey) {
        return SurveyVoteResponseDto.builder()
                .surveyId(survey.getId())
                .question(survey.getQuestion())
                .optionA(survey.getOptionA())
                .optionB(survey.getOptionB())
                .optionC(survey.getOptionC())
                .voteA(0)
                .voteB(0)
                .voteC(0)
                .percentA(0.0)
                .percentB(0.0)
                .percentC(0.0)
                .build();
    }
}
