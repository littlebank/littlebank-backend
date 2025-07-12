package com.littlebank.finance.global.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommonCodeResponse {
    private Integer code;
    private String message;
}
