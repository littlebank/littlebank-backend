package com.littlebank.finance.domain.challenge.controller;

import com.littlebank.finance.domain.challenge.dto.request.ChallengeCreateRequestDto;
import com.littlebank.finance.domain.challenge.dto.response.ChallengeResponseDto;
import com.littlebank.finance.domain.challenge.service.ChallengeService;
import com.littlebank.finance.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api-user/challenge")
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;

    @Operation(summary = "챌린지 생성")
    @PostMapping("/create")
    public ResponseEntity<ChallengeResponseDto> createChallenge(
            @RequestBody ChallengeCreateRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails user
            ) {
        ChallengeResponseDto response = challengeService.createChallenge(user.getId(), dto);
        return ResponseEntity.ok(response);
    }
}
