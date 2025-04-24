package com.littlebank.finance.global.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    private final static String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    @Value("${jwt.refreshToken-validity-in-seconds}")
    private long refreshTokenValidityInSecond;

    public ResponseCookie createCookie(String refreshToken) {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenValidityInSecond)
                .build();
    }

    public String getCookieValue(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(REFRESH_TOKEN_COOKIE_NAME)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public ResponseCookie deleteCookie() {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, "")
                .path("/")
                .httpOnly(true)
                .maxAge(0)  // 즉시 만료
                .build();
    }

}
