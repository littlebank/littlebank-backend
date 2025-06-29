package com.littlebank.finance.domain.subscription.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class SubscriptionCreateRequestDto {
    @NotNull
    private int seat;
    private boolean includeOwner;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
