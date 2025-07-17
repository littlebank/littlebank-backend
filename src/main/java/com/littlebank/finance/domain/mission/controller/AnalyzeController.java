package com.littlebank.finance.domain.mission.controller;

import com.littlebank.finance.domain.mission.dto.response.AnalyzeReportResponse;
import com.littlebank.finance.domain.mission.service.AnalyzeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-user/analyze")
@RequiredArgsConstructor
@Tag(name = "Analyze")
public class AnalyzeController {
    private final AnalyzeService analyzeService;

    @Operation(summary = "(부모) 총 분석 리포트 조회 API")
    @GetMapping("/{memberId}")
    public ResponseEntity<AnalyzeReportResponse> getAnalyzeReport(
            @Parameter(description = "아이의 member 식별 id")
            @PathVariable("memberId") Long memberId,
            @Parameter(description = "분석 기간", example = "14")
            @RequestParam("period") Integer period

    ) {
        AnalyzeReportResponse response = analyzeService.getAnalyzeReport(memberId, period);
        return ResponseEntity.ok(response);
    }
}
