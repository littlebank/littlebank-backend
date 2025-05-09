package com.littlebank.finance.domain.report.controller;

import com.littlebank.finance.domain.report.dto.request.ReportRequestDto;
import com.littlebank.finance.domain.report.dto.response.ReportResponseDto;
import com.littlebank.finance.domain.report.service.ReportService;
import com.littlebank.finance.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-user/report")
@RequiredArgsConstructor
@Tag(name = "Report")
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "신고 메일 전송 API")
    @PostMapping
    public ResponseEntity<ReportResponseDto> reportContent(
            @RequestBody ReportRequestDto request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        ReportResponseDto response = reportService.report(request, customUserDetails.getId());
        return ResponseEntity.ok(response);
    }
}