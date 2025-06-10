package com.littlebank.finance.domain.chat.controller;

import com.littlebank.finance.domain.chat.domain.ChatMessage;
import com.littlebank.finance.domain.chat.dto.request.ChatMessageRequest;
import com.littlebank.finance.domain.chat.dto.response.ChatMessageResponse;
import com.littlebank.finance.domain.chat.service.ChatMessageService;
import com.littlebank.finance.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatMessageController {
    private final static String CHAT_SUBSCRIBE_BASE_URL = "/sub/chat/";
    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat-send")
    public void sendMessage(@Payload ChatMessageRequest request, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        ChatMessage savedMessage = chatMessageService.saveMessage(customUserDetails.getId(), request);

        ChatMessageResponse response = ChatMessageResponse.of(savedMessage);

        // 채팅방 구독 경로로 메시지 브로드캐스트 "/sub/chat/{roomId}"
        messagingTemplate.convertAndSend(CHAT_SUBSCRIBE_BASE_URL + response.getRoomId(), response);
    }
}
