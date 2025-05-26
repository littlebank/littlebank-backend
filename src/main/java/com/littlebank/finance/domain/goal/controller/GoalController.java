package com.littlebank.finance.domain.goal.controller;

import com.littlebank.finance.domain.goal.dto.request.GoalApplyRequest;
import com.littlebank.finance.domain.goal.dto.response.ChildGoalResponse;
import com.littlebank.finance.domain.goal.dto.response.P3CommonGoalResponse;
import com.littlebank.finance.domain.goal.dto.response.WeeklyGoalResponse;
import com.littlebank.finance.domain.goal.service.GoalService;
import com.littlebank.finance.global.common.CustomPageResponse;
import com.littlebank.finance.global.common.PaginationPolicy;
import com.littlebank.finance.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api-user/goal")
@RequiredArgsConstructor
@Tag(name = "Goal")
public class GoalController {
    private final GoalService goalService;

    @Operation(summary = "(아이)목표 신청하기 API", description = "아이가 부모에게 목표 신청")
    @PostMapping("/child/apply")
    public ResponseEntity<P3CommonGoalResponse> applyGoal(
            @RequestBody @Valid GoalApplyRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        P3CommonGoalResponse response = goalService.applyGoal(customUserDetails.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "(아이)이번 주 목표 조회 API")
    @GetMapping("/child/weekly")
    public ResponseEntity<List<WeeklyGoalResponse>> getWeeklyGoal(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        List<WeeklyGoalResponse> response = goalService.getWeeklyGoal(customUserDetails.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "(부모)이번 주 아이들의 목표 조회 API")
    @GetMapping("/parent/weekly/{familyId}")
    public ResponseEntity<List<ChildGoalResponse>> getChildWeeklyGoal(
            @Parameter(description = "목표를 조회할 가족 식별 id")
            @PathVariable("familyId") Long familyId
    ) {
        List<ChildGoalResponse> response = goalService.getChildWeeklyGoal(familyId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "(부모)아이들의 모든 목표 조회 API")
    @GetMapping("/parent/all/{familyId}")
    public ResponseEntity<CustomPageResponse<ChildGoalResponse>> getChildGoals(
            @Parameter(description = "목표를 조회할 가족 식별 id")
            @PathVariable("familyId") Long familyId,
            @Parameter(description = "페이지 번호, 0부터 시작")
            @RequestParam("pageNumber") Integer pageNumber
    ) {
        Pageable pageable = PageRequest.of(pageNumber, PaginationPolicy.GENERAL_PAGE_SIZE);
        CustomPageResponse<ChildGoalResponse> response = goalService.getChildGoals(familyId, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "(부모)목표 신청 수락하기 API", description = "아이가 신청한 목표를 부모가 수락")
    @PatchMapping("/parent/apply/accept/{goalId}")
    public ResponseEntity<P3CommonGoalResponse> acceptApplyGoal(
            @Parameter(description = "신청을 수락할 목표 식별 id")
            @PathVariable("goalId") Long goalId
    ) {
        P3CommonGoalResponse response = goalService.acceptApplyGoal(goalId);
        return ResponseEntity.ok(response);
    }
}
