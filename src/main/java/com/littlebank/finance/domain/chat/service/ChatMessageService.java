package com.littlebank.finance.domain.chat.service;

import com.littlebank.finance.domain.chat.domain.*;
import com.littlebank.finance.domain.chat.domain.constant.EventType;
import com.littlebank.finance.domain.chat.domain.constant.RoomRange;
import com.littlebank.finance.domain.chat.domain.repository.*;
import com.littlebank.finance.domain.chat.dto.request.ChatMessageRequest;
import com.littlebank.finance.domain.chat.dto.request.ChatReadRequest;
import com.littlebank.finance.domain.chat.dto.request.RoomInviteRequest;
import com.littlebank.finance.domain.chat.dto.request.RoomLeaveRequest;
import com.littlebank.finance.domain.chat.dto.response.RoomLeaveResponse;
import com.littlebank.finance.domain.chat.exception.ChatException;
import com.littlebank.finance.domain.chat.service.async.AsyncChatMessageService;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.business.ChatPolicy;
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
    private final ChatRoomEventLogRepository chatRoomEventLogRepository;
    private final ChatRoomEventLogDetailRepository chatRoomEventLogDetailRepository;
    private final AsyncChatMessageService asyncChatMessageService;

    public ChatMessage saveMessage(Long userId, ChatMessageRequest request) {
        userChatRoomRepository.updateDisplayIdxByRoomId(request.getRoomId()); // 밑에 로직이 에러가 발생하지 않는다는 것을 가정 (개선 필요)

        User sender = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        ChatRoom room = chatRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ChatException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        // 1:1 채팅방에 나간 상태일 때
        int participantCount = 0;
        if (room.getRange() == RoomRange.PRIVATE) {
            UserChatRoom participant = userChatRoomRepository.findByUserIdAndRoomId(sender.getId(), room.getId()).orElse(null);
            if (participant != null && !participant.getIsJoined()) {
                participant.joinPrivateRoom();
            }
            participantCount = 1;
        } else if (room.getRange() == RoomRange.PRIVATE) {
            participantCount = userChatRoomRepository.countParticipantsExcludingUser(room.getId(), sender.getId()); // room 인원 수 컬럼 추가 및 개선
        }

        ChatMessage message = chatMessageRepository.save(ChatMessage.builder()
                .messageType(request.getMessageType())
                .room(room)
                .sender(sender)
                .content(request.getContent())
                .timestamp(LocalDateTime.now())
                .readCount(participantCount)
                .build());

        room.send(message);

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

    public ChatRoomEventLog inviteChatRoom(Long agentId, RoomInviteRequest request) {
        User agent = userRepository.findById(agentId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        ChatRoom room = chatRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ChatException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        if (room.getRange() == RoomRange.PRIVATE) {
            throw new ChatException(ErrorCode.CHATROOM_INVITE_GROUP_ONLY);
        }
        if (room.getParticipantNumber() + request.getTargetUserIds().size()
                > ChatPolicy.ROOM_PARTICIPANT_LIMIT_NUMBER) {
            throw new ChatException(ErrorCode.CHAT_ROOM_PARTICIPANT_LIMIT_EXCEEDED);
        }

        ChatRoomEventLog eventLog = chatRoomEventLogRepository.save(
                ChatRoomEventLog.builder()
                        .eventType(EventType.INVITE)
                        .room(room)
                        .agent(agent)
                        .build()
        );

        List<User> targetUsers = userRepository.findAllById(request.getTargetUserIds());
        targetUsers.stream()
                .forEach(u -> {
                    userChatRoomRepository.save(
                            UserChatRoom.builder()
                                    .room(room)
                                    .user(u)
                                    .build()
                    );
                    chatRoomEventLogDetailRepository.save(
                            ChatRoomEventLogDetail.builder()
                                    .log(eventLog)
                                    .targetUser(u)
                                    .build()
                    );
                });

        room.invite(request.getTargetUserIds().size());

        return eventLog;
    }

    public ChatRoomEventLog leaveChatRoom(Long agentId, RoomLeaveRequest request, RoomLeaveResponse response) {
        User agent = userRepository.findById(agentId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        ChatRoom room = chatRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ChatException(ErrorCode.CHAT_ROOM_NOT_FOUND));
        UserChatRoom participant = userChatRoomRepository.findByUserIdAndRoomId(agent.getId(), room.getId())
                .orElseThrow(() -> new ChatException(ErrorCode.USER_CHAT_ROOM_NOT_FOUND));

        if (room.getRange() == RoomRange.PRIVATE) {
            participant.leavePrivateRoom();
            return null;
        }

        ChatRoomEventLog eventLog = chatRoomEventLogRepository.save(
                ChatRoomEventLog.builder()
                        .eventType(EventType.LEAVE)
                        .room(room)
                        .agent(agent)
                        .build()
        );

        response.setEndOfDecreaseReadMarkMessageId(participant.getLastReadMessageId());
        chatMessageRepository.decreaseReadCountByUserChatRoom(participant);
        userChatRoomRepository.deleteById(participant.getId());

        room.leaveGroupRoom();

        return eventLog;
    }
    
    @Transactional(readOnly = true)
    public List<UserChatRoom> getChatRoomParticipantsExcludeTargetUserIds(Long roomId, List<Long> targetUserIds) {
        return userChatRoomRepository.findAllWithFetchByRoomIdNotInTargetUserIds(roomId, targetUserIds);
    }
}

