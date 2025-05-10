package com.littlebank.finance.domain.family.controller;

import com.littlebank.finance.domain.family.dto.request.FamilyMemberAddRequest;
import com.littlebank.finance.domain.family.dto.request.MyFamilyNicknameUpdateRequest;
import com.littlebank.finance.domain.family.dto.response.*;
import com.littlebank.finance.domain.family.service.FamilyService;
import com.littlebank.finance.domain.friend.dto.response.FamilyInvitationAcceptResponse;
import com.littlebank.finance.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api-user/family")
@RequiredArgsConstructor
@Tag(name = "Family")
public class FamilyController {
    private final FamilyService familyService;

    @Operation(summary = "맴버 추가 API")
    @PostMapping
    public ResponseEntity<FamilyMemberAddResponse> addFamilyMember(
            @RequestBody @Valid FamilyMemberAddRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        FamilyMemberAddResponse response = familyService.addFamilyMember(customUserDetails.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "내 가족 정보 조회 API")
    @GetMapping
    public ResponseEntity<FamilyInfoResponse> getFamilyInfo(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        FamilyInfoResponse response = familyService.getFamilyInfo(customUserDetails.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내 별명 설정 API")
    @PatchMapping("/me/nickname")
    public ResponseEntity<MyFamilyNicknameUpdateResponse> updateMyFamilyNickname(
            @RequestBody @Valid MyFamilyNicknameUpdateRequest request
    ) {
        MyFamilyNicknameUpdateResponse response = familyService.updateMyFamilyNickname(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "초대 받은 목록 조회 API")
    @GetMapping("/invite/received")
    public ResponseEntity<List<ReceivedFamilyInvitationResponse>> getReceivedFamilyInvitations(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        List<ReceivedFamilyInvitationResponse> response = familyService.getReceivedFamilyInvitations(customUserDetails.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "가족 그룹 소속 여부 확인 API")
    @GetMapping("/check-joined")
    public ResponseEntity<FamilyEnterCheckResponse> checkFamilyMembership(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        FamilyEnterCheckResponse response = familyService.checkFamilyMembership(customUserDetails.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "맴버 초대 수락 API")
    @PatchMapping("/invite/accept/{familyMemberId}")
    public ResponseEntity<FamilyInvitationAcceptResponse> acceptFamilyInvitation(
            @Parameter(description = "가족 구성원 id")
            @PathVariable("familyMemberId") Long familyMemberId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        FamilyInvitationAcceptResponse response = familyService.acceptFamilyInvitation(customUserDetails.getId(), familyMemberId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "초대 요청 중인 맴버 목록 조회 API")
    @GetMapping("/invite/sent/{familyId}")
    public ResponseEntity<List<SentFamilyInvitationResponse>> getSentFamilyInvitations(
            @Parameter(description = "가족 id")
            @PathVariable("familyId") Long familyId
    ) {
        List<SentFamilyInvitationResponse> response = familyService.getSentFamilyInvitations(familyId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "맴버 초대 거절 API")
    @DeleteMapping("/invite/refuse/{familyMemberId}")
    public ResponseEntity<Void> refuseFamilyInvitation(
            @Parameter(description = "삭제할 가족 구성원 id")
            @PathVariable("familyMemberId") Long familyMemberId
    ) {
        familyService.refuseFamilyInvitation(familyMemberId);
        return ResponseEntity.noContent().build();
    }
}
