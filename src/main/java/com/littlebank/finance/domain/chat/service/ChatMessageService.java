package com.littlebank.finance.domain.chat.service;

import com.littlebank.finance.domain.chat.domain.ChatMessage;
import com.littlebank.finance.domain.chat.domain.ChatRoom;
import com.littlebank.finance.domain.chat.domain.repository.ChatMessageRepository;
import com.littlebank.finance.domain.chat.domain.repository.ChatRoomRepository;
import com.littlebank.finance.domain.chat.dto.request.ChatMessageRequest;
import com.littlebank.finance.domain.chat.exception.ChatException;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ChatMessage saveMessage(Long userId, ChatMessageRequest request) {
        User sender = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        ChatRoom room = chatRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ChatException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        ChatMessage message = ChatMessage.builder()
                .messageType(request.getMessageType())
                .room(room)
                .sender(sender)
                .content(request.getContent())
                .timestamp(LocalDateTime.now())
                .build();

        return chatMessageRepository.save(message);
    }
}

