package com.littlebank.finance.domain.goal.controller;

import com.littlebank.finance.domain.goal.dto.request.GoalApplyRequest;
import com.littlebank.finance.domain.goal.dto.response.GoalApplyResponse;
import com.littlebank.finance.domain.goal.service.GoalService;
import com.littlebank.finance.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api-user/goal")
@RequiredArgsConstructor
@Tag(name = "Goal")
public class GoalController {
    private final GoalService goalService;

    @Operation(summary = "(아이)목표 신청하기 API", description = "아이가 부모에게 목표 신청")
    @PostMapping("/apply")
    public ResponseEntity<GoalApplyResponse> applyGoal(
            @RequestBody @Valid GoalApplyRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        GoalApplyResponse response = goalService.applyGoal(customUserDetails.getId(), request);
        return ResponseEntity.ok(response);
    }
}
