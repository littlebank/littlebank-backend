package com.littlebank.finance.domain.challenge.controller;

import com.littlebank.finance.domain.challenge.dto.request.ChallengeAdminRequestDto;
import com.littlebank.finance.domain.challenge.dto.response.ChallengeAdminResponseDto;
import com.littlebank.finance.domain.challenge.service.ChallengeService;
import com.littlebank.finance.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
//@PreAuthorize( "hasRole('ADMIN')")
@RequestMapping("/api-user/challenge")
@RequiredArgsConstructor
public class ChallengeAdminController {

    private final ChallengeService challengeService;

    @Operation(summary = "챌린지 생성")
    @PostMapping("/create")
    public ResponseEntity<ChallengeAdminResponseDto> createChallenge(
            @RequestBody ChallengeAdminRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails user
            ) {
        ChallengeAdminResponseDto response = challengeService.createChallenge(user.getId(), dto);
        return ResponseEntity.ok(response);
    }

}
