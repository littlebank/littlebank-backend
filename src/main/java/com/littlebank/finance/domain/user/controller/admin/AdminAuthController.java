package com.littlebank.finance.domain.user.controller.admin;

import com.littlebank.finance.domain.user.dto.request.LoginRequest;
import com.littlebank.finance.domain.user.dto.response.LoginResponse;
import com.littlebank.finance.domain.user.service.admin.AdminAuthService;
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
@RequestMapping("/api-admin/auth")
@RequiredArgsConstructor
@Tag(name = "Auth")
public class AdminAuthController {
    private final AdminAuthService adminAuthService;

    @Operation(summary = "어드민 로그인 API")
    @SecurityRequirements()
    @PostMapping("/public/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequest request
    ) {
        LoginResponse response = adminAuthService.login(request);
        return ResponseEntity.ok(response);
    }
}
