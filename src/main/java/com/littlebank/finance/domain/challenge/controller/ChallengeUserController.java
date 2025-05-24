package com.littlebank.finance.domain.challenge.controller;

import com.littlebank.finance.domain.challenge.domain.ChallengeCategory;
import com.littlebank.finance.domain.challenge.domain.ChallengeStatus;
import com.littlebank.finance.domain.challenge.dto.request.ChallengeUserRequestDto;
import com.littlebank.finance.domain.challenge.dto.response.admin.ChallengeAdminResponseDto;
import com.littlebank.finance.domain.challenge.dto.response.ChallengeUserResponseDto;
import com.littlebank.finance.domain.challenge.service.ChallengeService;
import com.littlebank.finance.global.common.CustomPageResponse;
import com.littlebank.finance.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-user/challenge")
@RequiredArgsConstructor
public class ChallengeUserController {

    private final ChallengeService challengeService;

    @Operation(summary = "챌린지 참여")
    @PostMapping("/join/{challengeId}")
    public ResponseEntity<ChallengeUserResponseDto> joinChallenge(
            @PathVariable Long challengeId,
            @RequestBody ChallengeUserRequestDto request,
            @AuthenticationPrincipal CustomUserDetails user) {
        ChallengeUserResponseDto response = challengeService.joinChallenge(user.getId(), challengeId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "챌린지 전체 조회")
    @GetMapping
    public ResponseEntity<CustomPageResponse<ChallengeAdminResponseDto>> getAllChallenges(
            @RequestParam(required = false) ChallengeCategory challengeCategory,
            @RequestParam(defaultValue = "0") int page,
            @AuthenticationPrincipal CustomUserDetails user
            ) {
        CustomPageResponse<ChallengeAdminResponseDto> response = challengeService.getChallenges(user.getId(), challengeCategory, page);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "챌린지 상세 조회")
    @GetMapping("/{challengeId}")
    public ResponseEntity<ChallengeAdminResponseDto> getChallengeDetial(
            @PathVariable Long challengeId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        ChallengeAdminResponseDto response = challengeService.getChallengeDetail(user.getId(), challengeId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내가 참여한 챌린지 조회")
    @GetMapping("/my")
    public ResponseEntity<CustomPageResponse<ChallengeAdminResponseDto>> getMyChallenges(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(required = true)ChallengeStatus challengeStatus,
            @RequestParam(defaultValue = "0") int page
    ) {
        CustomPageResponse<ChallengeAdminResponseDto> response = challengeService.getMyChallenges(user.getId(), challengeStatus, page);
        return ResponseEntity.ok(response);
    }
}
