package com.littlebank.finance.domain.quiz.controller.admin;

import com.littlebank.finance.domain.quiz.dto.request.CreateQuizRequestDto;
import com.littlebank.finance.domain.quiz.dto.response.CreateQuizResponseDto;
import com.littlebank.finance.domain.quiz.service.admin.AdminQuizService;
import com.littlebank.finance.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api-admin/quiz")
@Tag(name = "quiz")
@RequiredArgsConstructor
public class AdminQuizController {
    private final AdminQuizService adminQuizService;

    @Operation(summary = "퀴즈 생성")
    @PostMapping("/create")
    public ResponseEntity<CreateQuizResponseDto> createQuiz (
            @RequestBody CreateQuizRequestDto request,
            @AuthenticationPrincipal CustomUserDetails admin
            ) {
        CreateQuizResponseDto response = adminQuizService.createQuiz(admin.getId(), request);
        return ResponseEntity.ok(response);
    }
}
