package com.littlebank.finance.domain.user.controller;

import com.littlebank.finance.domain.user.dto.request.*;
import com.littlebank.finance.domain.user.dto.response.AccountHolderVerifyResponse;
import com.littlebank.finance.domain.user.dto.response.AccountPinVerifyResponse;
import com.littlebank.finance.domain.user.dto.response.LoginResponse;
import com.littlebank.finance.domain.user.dto.response.ReissueResponse;
import com.littlebank.finance.domain.user.service.AuthService;
import com.littlebank.finance.global.jwt.dto.TokenDto;
import com.littlebank.finance.global.security.CustomUserDetails;
import com.littlebank.finance.global.util.CookieUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-user/auth")
@RequiredArgsConstructor
@Tag(name = "Auth")
public class AuthController {
    private final AuthService authService;
    private final CookieUtil cookieUtil;

    @Operation(summary = "로그인 API", description = "refresh 토큰은 응답 헤더에서 파싱하여 저장")
    @SecurityRequirements()
    @PostMapping("/public/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequest request
    ) {
        TokenDto tokenDto = authService.login(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookieUtil.createCookie(tokenDto.getRefreshToken()).toString())
                .body(LoginResponse.of(tokenDto.getAccessToken()));
    }

    @Operation(summary = "로그아웃 API")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
            ) {
        authService.logout(customUserDetails.getId(), cookieUtil.getCookieValue(request));
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookieUtil.deleteCookie().toString())
                .build();
    }

    @Operation(summary = "access token 재발급 API")
    @PostMapping("/public/reissue")
    public ResponseEntity<ReissueResponse> reissue(
            @RequestBody @Valid ReissueRequest request
    ) {
        authService.reissue(request.getRefreshToken());
        return null;
    }

    @Operation(summary = "카카오 API 로그인", description = "카카오 인증 토큰(accessToken)을 담아서 요청")
    @SecurityRequirements()
    @PostMapping("/public/kakao/login")
    public ResponseEntity<LoginResponse> kakaoLogin(
            @RequestBody @Valid SocialLoginRequest request
    ) {
        TokenDto tokenDto = authService.kakaoLogin(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookieUtil.createCookie(tokenDto.getRefreshToken()).toString())
                .body(LoginResponse.of(tokenDto.getAccessToken()));
    }

    @Operation(summary = "네이버 API 로그인", description = "네이버 인증 토큰(accessToken)을 담아서 요청")
    @SecurityRequirements()
    @PostMapping("/public/naver/login")
    public ResponseEntity<LoginResponse> naverLogin(
            @RequestBody @Valid SocialLoginRequest request
    ) {
        TokenDto tokenDto = authService.naverLogin(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookieUtil.createCookie(tokenDto.getRefreshToken()).toString())
                .body(LoginResponse.of(tokenDto.getAccessToken()));
    }

    @Operation(summary = "계좌 검증 API")
    @PostMapping("/public/account/holder/verify")
    public ResponseEntity<AccountHolderVerifyResponse> verifyAccountHolder(
            @RequestBody @Valid AccountHolderVerifyRequest request
    ) {
        AccountHolderVerifyResponse holder = authService.verifyAccountHolder(request);
        return ResponseEntity.ok(holder);
    }

    @Operation(summary = "계좌 pin번호 검증 API")
    @PostMapping("/account/pin/verify")
    public ResponseEntity<AccountPinVerifyResponse> verifyAccountPin(
            @RequestBody @Valid AccountPinVerifyRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        AccountPinVerifyResponse response = authService.verifyAccountPin(customUserDetails.getId(), request);
        return ResponseEntity.ok(response);
    }
}
