package com.littlebank.finance.domain.family.controller;

import com.littlebank.finance.domain.family.dto.request.FamilyMemberAddRequest;
import com.littlebank.finance.domain.family.dto.response.FamilyMemberAddResponse;
import com.littlebank.finance.domain.family.service.FamilyService;
import com.littlebank.finance.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
