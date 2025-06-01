package com.littlebank.finance.domain.survey.controller.admin;

import com.littlebank.finance.domain.survey.dto.request.CreateSurveyRequestDto;
import com.littlebank.finance.domain.survey.dto.response.CreateSurveyResponseDto;
import com.littlebank.finance.domain.survey.service.admin.AdminSurveyService;
import com.littlebank.finance.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-admin/quiz")
@Tag(name = "quiz")
@RequiredArgsConstructor
public class AdminSurveyController {
    private final AdminSurveyService adminSurveyService;

    @Operation(summary = "퀴즈 생성")
    @PostMapping("/create")
    public ResponseEntity<CreateSurveyResponseDto> createQuiz (
            @RequestBody CreateSurveyRequestDto request,
            @AuthenticationPrincipal CustomUserDetails admin
            ) {
        CreateSurveyResponseDto response = adminSurveyService.createQuiz(admin.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "퀴즈 수정")
    @PutMapping("/update/{quizId}")
    public ResponseEntity<CreateSurveyResponseDto> updateQuiz (
            @PathVariable Long quizId,
            @RequestBody CreateSurveyRequestDto request,
            @AuthenticationPrincipal CustomUserDetails admin
    ) {
        CreateSurveyResponseDto response = adminSurveyService.updateQuiz(admin.getId(), quizId, request);
        return ResponseEntity.ok(response);
    }
}
