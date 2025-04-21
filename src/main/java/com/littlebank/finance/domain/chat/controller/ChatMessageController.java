package com.littlebank.finance.domain.chat.controller;

import com.littlebank.finance.domain.chat.dto.request.ChatMessageDto;
import com.littlebank.finance.domain.chat.dto.response.ChatMessageResponse;
import com.littlebank.finance.domain.chat.exception.ChatException;
import com.littlebank.finance.domain.chat.service.ChatService;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.service.UserService;
import com.littlebank.finance.global.error.exception.ErrorCode;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@Tag(name = "채팅방 메세지 SEND API", description = "채팅방 메세지 전송 기능")
@RestController
@SecurityRequirement(name = "JWT")
public class ChatMessageController {
    private final ChatService chatService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    public ChatMessageController(ChatService chatService, UserService userService, UserRepository userRepository, SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.userService = userService;
        this.userRepository=userRepository;
        this.messagingTemplate=messagingTemplate;
    }

    @MessageMapping("/api-user/chat.send.{roomId}")
    public void sendMessage(@DestinationVariable String roomId, @Payload ChatMessageDto dto, Principal principal) {
        String email = principal.getName();

        chatService.handleChatMessage(roomId, dto, email);
    }
}