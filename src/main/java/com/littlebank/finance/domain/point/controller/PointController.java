package com.littlebank.finance.domain.point.controller;

import com.littlebank.finance.domain.point.dto.request.PaymentInfoSaveRequest;
import com.littlebank.finance.domain.point.dto.response.PaymentHistoryResponse;
import com.littlebank.finance.domain.point.dto.response.PaymentInfoSaveResponse;
import com.littlebank.finance.domain.point.service.PointService;

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

@RestController
@RequestMapping("/api-user/point")
@RequiredArgsConstructor
@Tag(name = "Point")
public class PointController {
    private final PointService pointService;

    @Operation(summary = "포인트 충전 내역 저장 API")
    @PostMapping("/charge/payment/info")
    public ResponseEntity<PaymentInfoSaveResponse> verifyAndSave(
        @RequestBody @Valid PaymentInfoSaveRequest request,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        PaymentInfoSaveResponse response = pointService.verifyAndSave(customUserDetails.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "포인트 충전 내역 조회 API")
    @GetMapping("/charge/payment/history")
    public ResponseEntity<CustomPageResponse<PaymentHistoryResponse>> getPaymentHistory(
            @Parameter(description = "페이지 번호, 0부터 시작")
            @RequestParam(name = "pageNumber") Integer pageNumber,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Pageable pageable = PageRequest.of(pageNumber, PaginationPolicy.PAYMENT_HISTORY_PAGE_SIZE);
        CustomPageResponse<PaymentHistoryResponse> response = pointService.getPaymentHistory(customUserDetails.getId(), pageable);
        return ResponseEntity.ok(response);
    }
}
