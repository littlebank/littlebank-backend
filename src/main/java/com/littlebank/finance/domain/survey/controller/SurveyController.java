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
@RequestMapping("/api-user/survey")
@RequiredArgsConstructor
@Tag(name = "survey")
public class SurveyController {
    private final SurveyService surveyService;

    @Operation(summary = "설문 참여하기")
    @PostMapping("/join/{surveyId}")
    public ResponseEntity<SurveyVoteResponseDto> joinSurvey (
            @PathVariable Long surveyId,
            @RequestBody SurveyVoteRequestDto request,
            @AuthenticationPrincipal CustomUserDetails user
            ) {
        SurveyVoteResponseDto response = surveyService.joinSurvey(user.getId(), surveyId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "설문 조회")
    @GetMapping("/main")
    public ResponseEntity<SurveyVoteResponseDto> showSurvey (
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        SurveyVoteResponseDto response = surveyService.showSurvey(user.getId());
        return ResponseEntity.ok(response);
    }
}
