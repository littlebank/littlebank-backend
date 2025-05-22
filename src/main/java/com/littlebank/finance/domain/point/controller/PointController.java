package com.littlebank.finance.domain.point.controller;


import com.littlebank.finance.domain.point.dto.request.PaymentInfoSaveRequest;
import com.littlebank.finance.domain.point.dto.response.PaymentInfoSaveResponse;
import com.littlebank.finance.domain.point.service.PointService;

import com.littlebank.finance.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    @PostMapping("/charge/payment/save")
    public ResponseEntity<PaymentInfoSaveResponse> verifyAndSave(
        @RequestBody @Valid PaymentInfoSaveRequest request,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        PaymentInfoSaveResponse response = pointService.verifyAndSave(customUserDetails.getId(), request);
        return ResponseEntity.ok(response);
    }
}
