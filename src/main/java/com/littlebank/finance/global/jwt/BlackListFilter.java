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
public class BlackListFilter extends OncePerRequestFilter {
    private final CookieUtil cookieUtil;
    private final TokenProvider tokenProvider;
    private final RedisDao redisDao;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = request;
        String refreshToken = cookieUtil.getCookieValue(httpRequest);
        log.info("토큰 check : " + refreshToken);

        if (refreshToken != null && tokenProvider.validateToken(refreshToken)) {
            String blackListKey = RedisPolicy.BLACKLIST_KEY + refreshToken;
            String blacklistToken = redisDao.getValues(blackListKey);

            if (blacklistToken != null && blacklistToken.equals("registered")) {
                log.warn("블랙리스트에 등록된 RefreshToken입니다. 요청 차단. token={}", refreshToken);

                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
