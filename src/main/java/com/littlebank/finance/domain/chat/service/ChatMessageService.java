package com.littlebank.finance.domain.chat.service;

import com.littlebank.finance.domain.chat.domain.ChatMessage;
import com.littlebank.finance.domain.chat.domain.ChatRoom;
import com.littlebank.finance.domain.chat.domain.UserChatRoom;
import com.littlebank.finance.domain.chat.domain.repository.ChatMessageRepository;
import com.littlebank.finance.domain.chat.domain.repository.ChatRoomRepository;
import com.littlebank.finance.domain.chat.domain.repository.UserChatRoomRepository;
import com.littlebank.finance.domain.chat.dto.request.ChatMessageRequest;
import com.littlebank.finance.domain.chat.dto.request.ChatReadRequest;
import com.littlebank.finance.domain.chat.exception.ChatException;
import com.littlebank.finance.domain.chat.service.async.AsyncChatMessageService;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserChatRoomRepository userChatRoomRepository;
    private final AsyncChatMessageService asyncChatMessageService;

    public ChatMessage saveMessage(Long userId, ChatMessageRequest request) {
        userChatRoomRepository.updateDisplayIdxByRoomId(request.getRoomId()); // 밑에 로직이 에러가 발생하지 않는다는 것을 가정 (개선 필요)

        User sender = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        ChatRoom room = chatRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ChatException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        int participantCount = userChatRoomRepository.countParticipantsExcludingUser(room.getId(), sender.getId());


        ChatMessage message = chatMessageRepository.save(ChatMessage.builder()
                .messageType(request.getMessageType())
                .room(room)
                .sender(sender)
                .content(request.getContent())
                .timestamp(LocalDateTime.now())
                .readCount(participantCount)
                .build());

        room.updateLastMessageId(message);

        return message;
    }

    @Transactional(readOnly = true)
    public List<UserChatRoom> getChatRoomParticipants(Long roomId) {
        return userChatRoomRepository.findAllWithFetchByRoomId(roomId);
    }

    public void markMessagesAsRead(Long userId, ChatReadRequest request) {
        asyncChatMessageService.decreaseReadCounts(request.getMessageIds());
        asyncChatMessageService.updateLastReadMessageId(
                userId,
                request.getRoomId(),
                Collections.max(request.getMessageIds())
        );
    }

}

