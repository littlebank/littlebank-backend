package com.littlebank.finance.domain.chat.service;

import com.littlebank.finance.domain.chat.domain.ChatRoom;
import com.littlebank.finance.domain.chat.domain.UserChatRoom;
import com.littlebank.finance.domain.chat.domain.repository.ChatMessageRepository;
import com.littlebank.finance.domain.chat.domain.repository.ChatRoomRepository;
import com.littlebank.finance.domain.chat.domain.repository.UserChatRoomRepository;
import com.littlebank.finance.domain.chat.dto.request.ChatRoomCreateRequest;
import com.littlebank.finance.domain.chat.dto.response.APIMessageResponse;
import com.littlebank.finance.domain.chat.dto.response.ChatRoomCreateResponse;
import com.littlebank.finance.domain.chat.dto.response.ChatRoomDetailsResponse;
import com.littlebank.finance.domain.chat.dto.response.ChatRoomSummaryResponse;
import com.littlebank.finance.domain.chat.exception.ChatException;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.common.CustomPageResponse;
import com.littlebank.finance.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {
    private final static int CHAT_ROOM_MIN_PARTICIPANT_COUNT = 2;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserChatRoomRepository userChatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ChatRoomCreateResponse createRoom(Long userId, ChatRoomCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        ChatRoom chatRoom = chatRoomRepository.save(
                ChatRoom.builder()
                        .name(request.getName())
                        .range(request.getRoomRange())
                        .createdBy(user)
                        .build()
        );

        long resultCount = request.getParticipantIds().stream()
                .map(userRepository::findById)
                .flatMap(Optional::stream)
                .map(u -> userChatRoomRepository.save(
                                UserChatRoom.builder()
                                        .room(chatRoom)
                                        .user(u)
                                        .build()
                        )
                )
                .count();

        if (resultCount < CHAT_ROOM_MIN_PARTICIPANT_COUNT) {
            throw new ChatException(ErrorCode.CHAT_ROOM_TOO_FEW_PARTICIPANTS);
        }

        return ChatRoomCreateResponse.of(chatRoom);
    }

    @Transactional(readOnly = true)
    public List<ChatRoomSummaryResponse> getRoomList(Long userId) {
        return userChatRoomRepository.findChatRoomSummaryList(userId);
    }

    @Transactional(readOnly = true)
    public ChatRoomDetailsResponse getRoomDetails(Long userId, Long roomId) {
        return userChatRoomRepository.findChatRoomDetails(userId, roomId)
                .orElseThrow(() -> new ChatException(ErrorCode.USER_CHAT_ROOM_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public CustomPageResponse<APIMessageResponse> getMessages(Long userId, Long roomId, Long lastMessageId, Pageable pageable) {
        return CustomPageResponse.of(chatMessageRepository.findChatMessages(userId, roomId, lastMessageId, pageable));
    }
}