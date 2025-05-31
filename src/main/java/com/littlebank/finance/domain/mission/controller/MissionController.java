package com.littlebank.finance.domain.mission.controller;

import com.littlebank.finance.domain.mission.dto.request.CreateMissionRequestDto;
import com.littlebank.finance.domain.mission.dto.response.CommonMissionResponseDto;
import com.littlebank.finance.domain.mission.dto.response.MissionRecentRewardResponseDto;
import com.littlebank.finance.domain.mission.service.MissionService;
import com.littlebank.finance.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api-user/mission")
@RequiredArgsConstructor
@Tag(name = "mission")
public class MissionController {
    private final MissionService missionService;

    @Operation(summary = "(부모) 미션 생성하기 API", description = "부모가 아이의 미션 신청")
    @PostMapping("/create")
    public ResponseEntity<List<CommonMissionResponseDto>> createMission (
            @RequestBody @Valid CreateMissionRequestDto request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        List<CommonMissionResponseDto> response = missionService.createMission(request, user.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "(부모) 미션 생성 시 최근 보상 내역 조회 API")
    @GetMapping("/recent/reward/{childId}")
    public ResponseEntity<MissionRecentRewardResponseDto> getRecentReward (
            @PathVariable Long childId
    ) {
        MissionRecentRewardResponseDto response = missionService.getRecentReward(childId);
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "(아이) 미션 수락하기 API", description = "아이가 미션 수락")
    @PatchMapping("/child/apply/accept/{missionId}")
    public ResponseEntity<CommonMissionResponseDto> acceptMission (
            @PathVariable Long missionId
    ) {
        CommonMissionResponseDto response = missionService.acceptMission(missionId);
        return ResponseEntity.ok(response);
    }
}
