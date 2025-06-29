package com.littlebank.finance.domain.subscription.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SubscriptionStatusResponseDto {
    private boolean active;
    private LocalDateTime endDate;
    private boolean autoRenew;
}
