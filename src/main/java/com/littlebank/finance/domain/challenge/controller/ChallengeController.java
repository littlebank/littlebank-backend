package com.littlebank.finance.domain.challenge.controller;

import com.littlebank.finance.domain.challenge.domain.ChallengeCategory;
import com.littlebank.finance.domain.challenge.dto.request.ChallengeUserRequestDto;
import com.littlebank.finance.domain.challenge.dto.response.admin.ChallengeAdminResponseDto;
import com.littlebank.finance.domain.challenge.dto.response.ChallengeUserResponseDto;
import com.littlebank.finance.domain.challenge.service.ChallengeService;
import com.littlebank.finance.global.common.CustomPageResponse;
import com.littlebank.finance.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api-user/challenge")
@RequiredArgsConstructor
@Tag(name = "challenge")
public class ChallengeController {
    private final ChallengeService challengeService;

    @Operation(summary = "챌린지 참여하기 API")
    @PostMapping("/join/{challengeId}")
    public ResponseEntity<ChallengeUserResponseDto> joinChallenge(
            @PathVariable Long challengeId,
            @RequestBody ChallengeUserRequestDto request,
            @AuthenticationPrincipal CustomUserDetails user) {
        ChallengeUserResponseDto response = challengeService.joinChallenge(user.getId(), challengeId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "챌린지 전체 조회 API")
    @GetMapping
    public ResponseEntity<CustomPageResponse<ChallengeAdminResponseDto>> getAllChallenges(
            @RequestParam(required = false) ChallengeCategory challengeCategory,
            @RequestParam(defaultValue = "0") int page
            ) {
        CustomPageResponse<ChallengeAdminResponseDto> response = challengeService.getAllChallenges(challengeCategory, page);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내가 참여한 챌린지 조회 API")
    @GetMapping("/my")
    public ResponseEntity<CustomPageResponse<ChallengeUserResponseDto>> getMyChallenges(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(required = true) String type,
            @RequestParam(defaultValue = "0") int page
    ) {
        CustomPageResponse<ChallengeUserResponseDto> response = challengeService.getMyChallenges(user.getId(), type, page);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "(부모) 자녀가 신청한 챌린지 1개 조회 API")
    @GetMapping("/parent/requested/{participationId}")
    public ResponseEntity<ChallengeUserResponseDto> getChildRequestedChallenge (
        @PathVariable("participationId") Long participationId,
        @AuthenticationPrincipal CustomUserDetails parent
    ) {
        ChallengeUserResponseDto response = challengeService.getChildRequestedChallenge(parent.getId(), participationId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "(부모) 자녀의 챌린지 조회 API")
    @GetMapping("/parent/{familyId}/{childId}")
    public ResponseEntity<CustomPageResponse<ChallengeUserResponseDto>> getChildInProgressChallenge (
            @Parameter(description = "챌린지를 조회할 가족 식별 id")
            @PathVariable("familyId") Long familyId,
            @PathVariable("childId") Long childId,
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(defaultValue = "0") int page
    ) {
        CustomPageResponse<ChallengeUserResponseDto> response = challengeService.getChildChallenges(familyId, childId, user.getId(), page);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "(부모) 챌린지 신청 수락하기 API")
    @PatchMapping("/parent/apply/accept/{participationId}")
    public ResponseEntity<ChallengeUserResponseDto> acceptApplyChallenge (
            @Parameter(description = "신청을 수락할 챌린지참여 식별 id")
            @PathVariable("participationId") Long participationId,
            @AuthenticationPrincipal CustomUserDetails parent
    ) {
        ChallengeUserResponseDto response = challengeService.acceptApplyChallenge(participationId, parent.getId());
        return ResponseEntity.ok(response);
    }
}
