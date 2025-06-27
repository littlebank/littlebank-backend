package com.littlebank.finance.domain.goal.dto.request;

import com.littlebank.finance.domain.family.domain.Family;
import com.littlebank.finance.domain.goal.domain.Goal;
import com.littlebank.finance.domain.goal.domain.GoalCategory;
import com.littlebank.finance.domain.goal.domain.GoalStatus;
import com.littlebank.finance.domain.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GoalApplyRequest {
    @NotBlank
    @Schema(description = "목표 제목", example = "이번 주 저녁 설거지 담당")
    private String title;
    @NotNull
    @Schema(description = "목표 유형(학습인증, 습관형성)", example = "LEARNING or HABIT")
    private GoalCategory category;
    @NotNull
    @Schema(description = "포인트 보상금", example = "3000")
    private Integer reward;
    @NotNull
    @Schema(description = "시작 날짜(ISO 포맷 yyyy-MM-dd'T'HH:mm:ss)")
    private LocalDateTime startDate;
    @NotNull
    @Schema(description = "종료 날짜(ISO 포맷 yyyy-MM-dd'T'HH:mm:ss)")
    private LocalDateTime endDate;
    @NotNull
    @Schema(description = "가족 그룹 id", example = "1")
    private Long familyId;

    public Goal toEntity(User user, Family family) {
        return Goal.builder()
                .title(title)
                .category(category)
                .reward(reward)
                .startDate(startDate)
                .endDate(endDate)
                .status(GoalStatus.REQUESTED)
                .createdBy(user)
                .family(family)
                .build();
    }
}
