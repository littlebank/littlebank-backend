package com.littlebank.finance.domain.subscription.dto.response;


import com.littlebank.finance.domain.subscription.domain.TrialSubscription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class FreeSubscriptionResponseDto {
    private Long trialId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean used;

    public static FreeSubscriptionResponseDto of(TrialSubscription trialSubscription) {
        return FreeSubscriptionResponseDto.builder()
                .trialId(trialSubscription.getId())
                .startDate(trialSubscription.getStartDate())
                .endDate(trialSubscription.getEndDate())
                .used(trialSubscription.isUsed())
                .build();
    }
}
