package com.littlebank.finance.global.config;

import com.littlebank.finance.global.jwt.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
    @Slf4j
    @Configuration
    @EnableWebSocket
    @EnableWebSocketMessageBroker //stomp 사용 명시
    public static class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
        final TokenProvider tokenProvider;
        public WebSocketConfig(TokenProvider tokenProvider) {
            this.tokenProvider = tokenProvider;
        }
        @Override
        public void registerStompEndpoints(StompEndpointRegistry registry) {
            log.info("register stomp endpoint 실행 완료");
            registry.addEndpoint("/ws-chat")
                    .setAllowedOriginPatterns("*")
                    //.addInterceptors(new StompAuthenticationInterceptor(tokenProvider))
                    .withSockJS();
        }

        @Override
        public void configureMessageBroker(MessageBrokerRegistry registry) {
            log.info("configure message broker 실행 완료");
            registry.enableSimpleBroker("/topic"); //서버 -> 클라이언트
            registry.setApplicationDestinationPrefixes("/app"); //클라이언트 -> 서버
        }


        @Autowired
        private S3Config.StompChannelInterceptor stompChannelInterceptor;

        @Override
        public void configureClientInboundChannel(ChannelRegistration registration) {
            registration.interceptors(stompChannelInterceptor);
        }
    }
}
