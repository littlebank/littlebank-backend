package com.littlebank.finance.domain.subscription.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GoogleIAPResponseDto {
    private String productId;
    private LocalDateTime expiredTime;
    private String regionCode;
    private boolean acknowledged;
}


