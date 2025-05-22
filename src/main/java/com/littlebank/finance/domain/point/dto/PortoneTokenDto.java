package com.littlebank.finance.domain.point.dto;

import lombok.Getter;

@Getter
public class PortoneTokenDto {
    private int code;
    private String message;
    private TokenData response;

    @Getter
    public static class TokenData {
        private String access_token;
        private long now;
        private long expired_at;

        public String getAccessToken() {
            return access_token;
        }
    }
}
