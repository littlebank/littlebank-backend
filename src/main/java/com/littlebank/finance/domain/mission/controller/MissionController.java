package com.littlebank.finance.domain.mission.controller;

import com.littlebank.finance.domain.mission.dto.request.CreateMissionRequestDto;
import com.littlebank.finance.domain.mission.dto.response.CreateMissionResponseDto;
import com.littlebank.finance.domain.mission.service.MissionService;
import com.littlebank.finance.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api-user/mission")
@RequiredArgsConstructor
@Tag(name = "mission")
public class MissionController {
    private final MissionService missionService;

    @Operation(summary = "(부모) 미션 생성하기 API", description = "부모가 아이의 미션 신청")
    @PostMapping
    public ResponseEntity<CreateMissionResponseDto> createMission (
            @RequestBody @Valid CreateMissionRequestDto request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        CreateMissionResponseDto response = missionService.createMission(request, user.getId());
        return ResponseEntity.ok(response);
    }
}
