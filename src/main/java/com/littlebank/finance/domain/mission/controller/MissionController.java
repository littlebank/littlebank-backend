package com.littlebank.finance.domain.mission.controller;

import com.littlebank.finance.domain.mission.domain.MissionCategory;
import com.littlebank.finance.domain.mission.domain.MissionSubject;
import com.littlebank.finance.domain.mission.domain.MissionType;
import com.littlebank.finance.domain.mission.dto.request.CreateMissionRequestDto;
import com.littlebank.finance.domain.mission.dto.request.MissionRecentRewardRequestDto;
import com.littlebank.finance.domain.mission.dto.response.CommonMissionResponseDto;
import com.littlebank.finance.domain.mission.dto.response.MissionRecentRewardResponseDto;
import com.littlebank.finance.domain.mission.service.MissionService;
import com.littlebank.finance.global.common.CustomPageResponse;
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
    @GetMapping("/recent/reward")
    public ResponseEntity<MissionRecentRewardResponseDto> getRecentReward (
            @RequestParam Long childId,
            @RequestParam MissionCategory category,
            @RequestParam(required = false)MissionSubject subject
            ) {
        MissionRecentRewardRequestDto request = MissionRecentRewardRequestDto.builder()
                .type(MissionType.FAMILY)
                .category(category)
                .subject(subject)
                .build();
        MissionRecentRewardResponseDto response = missionService.getRecentReward(request, childId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "(부모) 아이의 미션 전체 조회 API")
    @GetMapping("/parent/all/{childId}")
    public ResponseEntity<CustomPageResponse<CommonMissionResponseDto>> getAllMissions (
            @PathVariable Long childId,
            @RequestParam(defaultValue = "0") int page
    ) {
        CustomPageResponse<CommonMissionResponseDto> response = missionService.getChildMissions(childId, page);
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

    @Operation(summary = "(아이) 나의 모든 미션 조회 API")
    @GetMapping("/child/all")
    public ResponseEntity<CustomPageResponse<CommonMissionResponseDto>> getMyMissions (
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(defaultValue = "0") int page
    ) {
        CustomPageResponse<CommonMissionResponseDto> response = missionService.getMyMissions(user.getId(), page);
        return ResponseEntity.ok(response);
    }
}
