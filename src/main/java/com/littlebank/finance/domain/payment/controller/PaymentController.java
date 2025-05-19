package com.littlebank.finance.domain.payment.controller;


import com.littlebank.finance.domain.payment.dto.request.PaymentInfoSaveRequest;
import com.littlebank.finance.domain.payment.dto.response.PaymentInfoSaveResponse;
import com.littlebank.finance.domain.payment.service.PaymentService;

import com.littlebank.finance.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-user/payment")
@RequiredArgsConstructor
@Tag(name = "Family")
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(summary = "결제 정보 저장 API")
    @PostMapping("/save")
    public ResponseEntity<PaymentInfoSaveResponse> verifyAndSave(
        @RequestBody @Valid PaymentInfoSaveRequest request,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        PaymentInfoSaveResponse response = paymentService.verifyAndSave(customUserDetails.getId(), request);
        return ResponseEntity.ok(response);
    }
}
