package com.littlebank.finance.domain.user.controller;

import com.littlebank.finance.domain.user.dto.request.EmailSendRequest;
import com.littlebank.finance.domain.user.dto.response.EmailSendResponse;
import com.littlebank.finance.domain.user.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api-user/mail")
@RequiredArgsConstructor
@Tag(name = "Mail")
public class MailController {
    private final MailService mailService;

    @Operation(summary = "이메일 확인 메일 발신 API")
    @PostMapping("/public/email/send")
    public ResponseEntity<EmailSendResponse> sendEMail(
            @RequestBody @Valid EmailSendRequest request
    ) {
        EmailSendResponse response = mailService.sendSimpleEmail(request);
        return ResponseEntity.ok(response);
    }
}
