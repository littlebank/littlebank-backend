package com.littlebank.finance.global.socket;

import com.littlebank.finance.global.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompChannelInterceptor implements ChannelInterceptor {
    private final TokenProvider tokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String bearerToken = accessor.getFirstNativeHeader("Authorization");

            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                String token = bearerToken.substring(7);

                if (tokenProvider.validateToken(token)) {
                    Authentication authentication = tokenProvider.getAuthentication(token);
                    if (authentication != null) {
                        accessor.setUser(authentication);
                        log.info("STOMP CONNECT - Authentication set for user: {}", authentication.getName());
                    } else {
                        log.warn("STOMP CONNECT - Authentication object is null for token: {}", token);
                    }
                } else {
                    log.warn("STOMP CONNECT - Invalid token: {}", token);
                }
            } else {
                log.warn("STOMP CONNECT - Authorization header missing or malformed.");
            }
        } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            log.info("STOMP DISCONNECT - SecurityContextHolder not used.");
        }
        return message;
    }
}