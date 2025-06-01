package com.littlebank.finance.domain.survey.controller;

import com.littlebank.finance.domain.survey.dto.request.SurveyVoteRequestDto;
import com.littlebank.finance.domain.survey.dto.response.SurveyVoteResponseDto;
import com.littlebank.finance.domain.survey.service.SurveyService;
import com.littlebank.finance.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-user/quiz")
@RequiredArgsConstructor
@Tag(name = "quiz")
public class SurveyController {
    private final SurveyService surveyService;

    @Operation(summary = "퀴즈 참여하기")
    @PostMapping("/join/{quizId}")
    public ResponseEntity<SurveyVoteResponseDto> joinQuiz (
            @PathVariable Long quizId,
            @RequestBody SurveyVoteRequestDto request,
            @AuthenticationPrincipal CustomUserDetails user
            ) {
        SurveyVoteResponseDto response = surveyService.joinQuiz(user.getId(), quizId, request);
        return ResponseEntity.ok(response);
    }
}
