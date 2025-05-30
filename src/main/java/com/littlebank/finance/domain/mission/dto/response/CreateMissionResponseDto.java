package com.littlebank.finance.domain.mission.dto.response;

import com.littlebank.finance.domain.mission.domain.Mission;
import com.littlebank.finance.domain.mission.domain.MissionSubject;
import com.littlebank.finance.domain.mission.domain.MissionType;
import com.littlebank.finance.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CreateMissionResponseDto {
    private Long missionId;
    private String title;
    private MissionType type; // 가족 미션, 학원 미션
    private MissionSubject subject;
    private int reward;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<Long> childIds;

    public static CreateMissionResponseDto of(Mission mission) {
        List<Long> childIds = mission.getAssignments().stream()
                .map(assignment -> assignment.getChild().getId())
                .toList();

        return CreateMissionResponseDto.builder()
                .missionId(mission.getId())
                .title(mission.getTitle())
                .type(mission.getType())
                .subject(mission.getSubject())
                .reward(mission.getReward())
                .startDate(mission.getStartDate())
                .endDate(mission.getEndDate())
                .childIds(childIds)
                .build();
    }
}
