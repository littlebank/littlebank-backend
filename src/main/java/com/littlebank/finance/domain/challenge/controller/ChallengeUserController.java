package com.littlebank.finance.domain.challenge.controller;

import com.littlebank.finance.domain.challenge.domain.ChallengeCategory;
import com.littlebank.finance.domain.challenge.dto.request.ChallengeUserRequestDto;
import com.littlebank.finance.domain.challenge.dto.response.ChallengeAdminResponseDto;
import com.littlebank.finance.domain.challenge.dto.response.ChallengeUserResponseDto;
import com.littlebank.finance.domain.challenge.service.ChallengeService;
import com.littlebank.finance.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<Page<ChallengeAdminResponseDto>> getAllChallenges(
            @RequestParam(required = false) ChallengeCategory challengeCategory,
            @PageableDefault(size = 10, sort = "createdDate", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails user
            ) {
        Page<ChallengeAdminResponseDto> response = challengeService.getChallenges(user.getId(), challengeCategory, pageable);
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
}
