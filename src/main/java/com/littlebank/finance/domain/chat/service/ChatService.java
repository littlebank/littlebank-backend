package com.littlebank.finance.domain.chat.service;

import com.littlebank.finance.domain.chat.domain.ChatRoom;
import com.littlebank.finance.domain.chat.domain.ChatRoomEventLog;
import com.littlebank.finance.domain.chat.domain.ChatRoomEventLogDetail;
import com.littlebank.finance.domain.chat.domain.UserChatRoom;
import com.littlebank.finance.domain.chat.domain.constant.EventType;
import com.littlebank.finance.domain.chat.domain.constant.RoomRange;
import com.littlebank.finance.domain.chat.domain.repository.*;
import com.littlebank.finance.domain.chat.dto.UserFriendInfoDto;
import com.littlebank.finance.domain.chat.dto.request.ChatRoomCreateRequest;
import com.littlebank.finance.domain.chat.dto.response.*;
import com.littlebank.finance.domain.chat.exception.ChatException;
import com.littlebank.finance.domain.friend.domain.repository.FriendRepository;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.business.ChatPolicy;
import com.littlebank.finance.global.common.CustomPageResponse;
import com.littlebank.finance.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {
    private final static int CHAT_ROOM_MIN_PARTICIPANT_COUNT = 2;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserChatRoomRepository userChatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomEventLogRepository chatRoomEventLogRepository;
    private final ChatRoomEventLogDetailRepository chatRoomEventLogDetailRepository;
    private final FriendRepository friendRepository;

    public ChatRoomCreateResponse createRoom(Long userId, ChatRoomCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        ChatRoom room = chatRoomRepository.save(
                ChatRoom.builder()
                        .name(request.getName())
                        .range(request.getRoomRange())
                        .createdBy(user)
                        .build()
        );

        ChatRoomEventLog eventLog;
        if (room.getRange() == RoomRange.GROUP) {
            eventLog = chatRoomEventLogRepository.save(
                    ChatRoomEventLog.builder()
                            .eventType(EventType.INVITE)
                            .room(room)
                            .agent(user)
                            .build()
            );
        } else {
            eventLog = null;
        }

        long joinedCount = request.getParticipantIds().stream()
                .map(userRepository::findById)
                .flatMap(Optional::stream)
                .map(u -> {
                    UserChatRoom joinedUser = userChatRoomRepository.save(
                                    UserChatRoom.builder()
                                            .room(room)
                                            .user(u)
                                            .build()
                            );

                    if (room.getRange() != RoomRange.PRIVATE && joinedUser.getUser().getId() != userId) {
                        chatRoomEventLogDetailRepository.save(
                                ChatRoomEventLogDetail.builder()
                                        .log(eventLog)
                                        .targetUser(u)
                                        .build()
                        );
                    }

                    return joinedUser;
                })
                .count();

        if (joinedCount < CHAT_ROOM_MIN_PARTICIPANT_COUNT) {
            throw new ChatException(ErrorCode.CHAT_ROOM_TOO_FEW_PARTICIPANTS);
        }

        return ChatRoomCreateResponse.of(room);
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

    @Transactional(readOnly = true)
    public List<EventLogResponse> getEventLog(Long userId, Long roomId, Boolean isOnlyOnePage, Long startMessageId, Long endMessageId) {
        List<ChatRoomEventLog> eventLogs;
        if (isOnlyOnePage) {
            eventLogs = chatRoomEventLogRepository.findAllByRoomId(userId, roomId);
        } else {
            eventLogs = chatRoomEventLogRepository.findByRoomIdAndMessageIds(userId, roomId, startMessageId, endMessageId);
        }

        List<EventLogResponse> responses = eventLogs.stream().map(eventLog -> {
            String message = "";
            System.out.println(eventLog.getId());
            if (eventLog.getEventType() == EventType.INVITE) {
                UserFriendInfoDto agent = friendRepository.findUserFriendInfoDto(userId, eventLog.getAgent().getId()).orElse(null);
                List<UserFriendInfoDto> targets =
                        friendRepository.findUserFriendInfoDtoList(
                                userId,
                                eventLog.getEventLogDetails().stream()
                                        .map(e -> e.getTargetUser().getId())
                                        .collect(Collectors.toList())
                        );

                message = ChatPolicy.getInvitationMessage(agent, targets);
            } else if (eventLog.getEventType() == EventType.LEAVE) {
                UserFriendInfoDto agent = friendRepository.findUserFriendInfoDto(userId, eventLog.getAgent().getId()).orElse(null);
                message = ChatPolicy.getLeaveMessage(agent);
            }
            return EventLogResponse.of(message, eventLog);
        }).collect(Collectors.toList());

        return responses;
    }
}