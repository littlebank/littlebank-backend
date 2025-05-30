package com.littlebank.finance.domain.mission.dto.request;

import com.littlebank.finance.domain.mission.domain.Mission;
import com.littlebank.finance.domain.mission.domain.MissionSubject;
import com.littlebank.finance.domain.mission.domain.MissionType;
import com.littlebank.finance.domain.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CreateMissionRequestDto {
    @NotBlank
    @Schema(description = "미션 제목", example = "수학 이차방정식 88p까지 풀어오기")
    private String title;
    @NotNull
    @Schema(description = "미션 수행 과목", example = "수학")
    private MissionSubject subject;
    @NotNull
    @Schema(description = "미션 보상", example = "10000")
    private int reward;
    @NotNull
    @Schema(description = "시작 날짜(ISO 포맷 yyyy-MM-dd'T'HH:mm:ss)")
    private LocalDateTime startDate;
    @NotNull
    @Schema(description = "종료 날짜(ISO 포맷 yyyy-MM-dd'T'HH:mm:ss)")
    private LocalDateTime endDate;
    @NotNull
    @Schema(description = "미션 보내는 자식 id", example = "[1,2]")
    private List<Long> childIds;

    public Mission toEntity(User user, MissionType type) {
        return Mission.builder()
                .title(title)
                .subject(subject)
                .type(type)
                .reward(reward)
                .startDate(startDate)
                .endDate(endDate)
                .createdBy(user)
                .isDeleted(false)
                .build();
    }
}

