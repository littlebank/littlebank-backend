package com.littlebank.finance.domain.ranking.dto.response;

import com.littlebank.finance.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TargetResponseDto {
    private Long userId;
    private String userName;
    private Integer targetAmount;

    public static TargetResponseDto of(User user, Integer targetAmount) {
        return TargetResponseDto.builder()
                .userId(user.getId())
                .userName(user.getName())
                .targetAmount(targetAmount)
                .build();
    }
}
