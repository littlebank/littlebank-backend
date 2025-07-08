package com.littlebank.finance.domain.ranking.controller;


import com.littlebank.finance.domain.ranking.dto.request.TargetRequestDto;
import com.littlebank.finance.domain.ranking.dto.response.GoalRankingResponseDto;
import com.littlebank.finance.domain.ranking.dto.response.TargetResponseDto;
import com.littlebank.finance.domain.ranking.service.RankingService;
import com.littlebank.finance.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequestMapping("/api-user/ranking")
@RequiredArgsConstructor
@Tag(name = "ranking")
public class RankingController {
    private final RankingService rankingService;

    @Operation(summary = "목표 금액대 입력 또는 수정")
    @PostMapping("/amount")
    public ResponseEntity<TargetResponseDto> setTargetAmount(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody TargetRequestDto request
    ) {
        TargetResponseDto response = rankingService.setTargetAmount(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "목표 금액대 기준 친구 비교 랭킹 조회")
    @GetMapping("/goal/friends/{targetId}")
    public ResponseEntity<GoalRankingResponseDto> getGoalRanking(
            @PathVariable Long targetId,
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM") YearMonth month
    ) {
        YearMonth queryMonth = month != null ? month : YearMonth.now();
        GoalRankingResponseDto response = rankingService.getGoalRankingByTargetAmount(user.getId(), targetId, queryMonth);
        return ResponseEntity.ok(response);
    }
}
