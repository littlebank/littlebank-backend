package com.littlebank.finance.domain.point.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class LatestSentAccountResponse {
    private Long userId;
    private String userName;
    private String bankName;
    private String bankCode;
    private String bankAccount;
    private LocalDateTime sentAt;
    // private Boolean isFavorite; 추가 예정
}
