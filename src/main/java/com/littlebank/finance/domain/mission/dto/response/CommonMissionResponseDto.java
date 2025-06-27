package com.littlebank.finance.domain.mission.dto.response;

import com.littlebank.finance.domain.mission.domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class CommonMissionResponseDto {
    private Long missionId;
    private String title;
    private MissionType type; // 가족 미션, 학원 미션
    private MissionCategory category; // 학습인증, 습관형성
    private MissionSubject subject;
    private MissionStatus status;
    private Integer reward;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long createdBy;
    private Long childId;
    private boolean isDeleted;

    public static CommonMissionResponseDto of(Mission mission) {
        return CommonMissionResponseDto.builder()
                .missionId(mission.getId())
                .title(mission.getTitle())
                .type(mission.getType())
                .category(mission.getCategory())
                .subject(mission.getSubject())
                .status(mission.getStatus())
                .reward(mission.getReward())
                .startDate(mission.getStartDate())
                .endDate(mission.getEndDate())
                .createdBy(mission.getCreatedBy().getId())
                .childId(mission.getChild().getId())
                .isDeleted(mission.getIsDeleted())
                .build();
    }
}
