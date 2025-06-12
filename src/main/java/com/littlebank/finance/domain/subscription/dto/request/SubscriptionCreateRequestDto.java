package com.littlebank.finance.domain.subscription.dto.request;

import com.littlebank.finance.domain.user.domain.User;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SubscriptionCreateRequestDto {
    @NotNull
    private int seat;
}
