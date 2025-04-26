package com.littlebank.finance.global.jwt;

import com.littlebank.finance.global.redis.RedisDao;
import com.littlebank.finance.global.redis.RedisPolicy;
import com.littlebank.finance.global.util.CookieUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LogoutFilter extends OncePerRequestFilter {
    private final CookieUtil cookieUtil;
    private final TokenProvider tokenProvider;
    private final RedisDao redisDao;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = request;
        String refreshToken = cookieUtil.getCookieValue(httpRequest);
        log.info("refresh 토큰 check : " + refreshToken);

        if (refreshToken != null && tokenProvider.validateToken(refreshToken)) {
            String loginUserKey = RedisPolicy.LOGIN_USER_KEY_PREFIX + tokenProvider.getAuthentication(refreshToken).getName();

            if (!redisDao.existData(loginUserKey) || !refreshToken.equals(redisDao.getValues(loginUserKey))) {
                log.warn("로그아웃된 상태입니다. token={}", refreshToken);

                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
