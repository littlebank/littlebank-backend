package com.littlebank.finance.domain.point.controller;

import com.littlebank.finance.domain.point.dto.request.PaymentInfoSaveRequest;
import com.littlebank.finance.domain.point.dto.request.PointRefundRequest;
import com.littlebank.finance.domain.point.dto.request.PointTransferRequest;
import com.littlebank.finance.domain.point.dto.response.*;
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
import org.springframework.http.HttpStatus;
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
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "포인트 충전 내역 조회 API")
    @GetMapping("/charge/payment/history")
    public ResponseEntity<CustomPageResponse<PaymentHistoryResponse>> getPaymentHistory(
            @Parameter(description = "페이지 번호, 0부터 시작")
            @RequestParam(name = "pageNumber") Integer pageNumber,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Pageable pageable = PageRequest.of(pageNumber, PaginationPolicy.GENERAL_PAGE_SIZE);
        CustomPageResponse<PaymentHistoryResponse> response = pointService.getPaymentHistory(customUserDetails.getId(), pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "포인트 이체 API")
    @PostMapping("/transfer")
    public ResponseEntity<CommonPointTransferResponse> transferPoint(
            @RequestBody @Valid PointTransferRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        CommonPointTransferResponse response = pointService.transferPoint(customUserDetails.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "들어온 포인트 내역 조회 API")
    @GetMapping("/transfer/receive/history")
    public ResponseEntity<CustomPageResponse<ReceivePointHistoryResponse>> getReceivedPointHistory(
            @Parameter(description = "페이지 번호, 0부터 시작")
            @RequestParam(name = "pageNumber") Integer pageNumber,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Pageable pageable = PageRequest.of(pageNumber, PaginationPolicy.GENERAL_PAGE_SIZE);
        CustomPageResponse<ReceivePointHistoryResponse> response = pointService.getReceivedPointHistory(customUserDetails.getId(), pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "나간 포인트 내역 조회 API")
    @GetMapping("/transfer/sent/history")
    public ResponseEntity<CustomPageResponse<SendPointHistoryResponse>> getSentPointHistory(
            @Parameter(description = "페이지 번호, 0부터 시작")
            @RequestParam(name = "pageNumber") Integer pageNumber,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Pageable pageable = PageRequest.of(pageNumber, PaginationPolicy.GENERAL_PAGE_SIZE);
        CustomPageResponse<SendPointHistoryResponse> response = pointService.getSentPointHistory(customUserDetails.getId(), pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "최근 포인트를 보낸 계좌 조회 API")
    @GetMapping("/transfer/latest/account")
    public ResponseEntity<CustomPageResponse<LatestSentAccountResponse>> getLatestSentAccount(
            @Parameter(description = "페이지 번호, 0부터 시작")
            @RequestParam(name = "pageNumber") Integer pageNumber,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Pageable pageable = PageRequest.of(pageNumber, PaginationPolicy.GENERAL_PAGE_SIZE);
        CustomPageResponse<LatestSentAccountResponse> response = pointService.getLatestSentAccount(customUserDetails.getId(), pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "포인트 꺼내기 API")
    @PostMapping("/refund")
    public ResponseEntity<PointRefundResponse> refundPoint(
            @RequestBody @Valid PointRefundRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        PointRefundResponse response = pointService.refundPoint(customUserDetails.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "포인트 환전 대기 목록 조회 API")
    @GetMapping("/refund/status-wait")
    public ResponseEntity<CustomPageResponse<WaitStatusRefundResponse>> getRefundWaitStatus(
            @Parameter(description = "페이지 번호, 0부터 시작")
            @RequestParam(name = "pageNumber") Integer pageNumber,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Pageable pageable = PageRequest.of(pageNumber, PaginationPolicy.GENERAL_PAGE_SIZE);
        CustomPageResponse<WaitStatusRefundResponse> response = pointService.getRefundWaitStatus(customUserDetails.getId(), pageable);
        return ResponseEntity.ok(response);
    }
}
