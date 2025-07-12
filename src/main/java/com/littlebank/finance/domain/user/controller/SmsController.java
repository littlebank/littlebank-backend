package com.littlebank.finance.domain.user.controller;

import com.littlebank.finance.domain.user.dto.request.CertificationCodeSendRequest;
import com.littlebank.finance.domain.user.service.SmsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api-user/sms")
@RequiredArgsConstructor
@Tag(name = "Sms")
public class SmsController {
    private final SmsService smsService;

    @Operation(summary = "휴대폰 인증번호 발신 API")
    @SecurityRequirements()
    @PostMapping("/public/certification-code/send")
    public ResponseEntity<?> sendCertificationCode(
            @RequestBody @Valid CertificationCodeSendRequest request
    ) {
        smsService.sendCertificationCode(request);
        return ResponseEntity.ok("인증번호 발신 완료");
    }

}
