package com.littlebank.finance.domain.family.controller;

import com.littlebank.finance.domain.family.dto.request.FamilyMemberAddRequest;
import com.littlebank.finance.domain.family.dto.request.MyFamilyNicknameUpdateRequest;
import com.littlebank.finance.domain.family.dto.response.FamilyInfoResponse;
import com.littlebank.finance.domain.family.dto.response.FamilyInvitationResponse;
import com.littlebank.finance.domain.family.dto.response.FamilyMemberAddResponse;
import com.littlebank.finance.domain.family.dto.response.MyFamilyNicknameUpdateResponse;
import com.littlebank.finance.domain.family.service.FamilyService;
import com.littlebank.finance.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
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
    public ResponseEntity<List<FamilyInvitationResponse>> getReceivedFamilyInvitations(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        List<FamilyInvitationResponse> response = familyService.getReceivedFamilyInvitations(customUserDetails.getId());
        return ResponseEntity.ok(response);
    }
}
