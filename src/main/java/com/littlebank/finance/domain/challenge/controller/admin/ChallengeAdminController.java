package com.littlebank.finance.domain.challenge.controller.admin;

import com.littlebank.finance.domain.challenge.dto.request.admin.ChallengeAdminRequestDto;
import com.littlebank.finance.domain.challenge.dto.response.admin.ChallengeAdminResponseDto;
import com.littlebank.finance.domain.challenge.service.ChallengeService;
import com.littlebank.finance.domain.challenge.service.admin.ChallengeAdminService;
import com.littlebank.finance.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
//@PreAuthorize( "hasRole('ADMIN')")
@RequestMapping("/api-user/challenge")
@RequiredArgsConstructor
public class ChallengeAdminController {

    private final ChallengeAdminService challengeAdminService;

    @Operation(summary = "챌린지 생성")
    @PostMapping("/create")
    public ResponseEntity<ChallengeAdminResponseDto> createChallenge(
            @RequestBody ChallengeAdminRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails user
            ) {
        ChallengeAdminResponseDto response = challengeAdminService.createChallenge(user.getId(), dto);
        return ResponseEntity.ok(response);
    }

}
