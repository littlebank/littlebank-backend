package com.littlebank.finance.domain.chat.service;

import com.littlebank.finance.domain.chat.dto.request.ChatRoomRequest;
import com.littlebank.finance.domain.chat.domain.ChatRoom;
import com.littlebank.finance.domain.chat.domain.ChatRoomParticipant;
import com.littlebank.finance.domain.chat.domain.repository.ChatRoomParticipantRepository;
import com.littlebank.finance.domain.chat.domain.repository.ChatRoomRepository;
import com.littlebank.finance.domain.chat.exception.ChatException;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomParticipantRepository participantRepository;
    private final UserRepository userRepository;

    public ChatRoom createRoom(ChatRoomRequest request, Long currentUserId) {
        Set<Long> participantIds=new HashSet<>(request.getParticipantIds());
        participantIds.add(currentUserId);

        //기존 채팅방 존재 여부 확인
        if (participantIds.size() == 2) {
            List<String> existingRoomIds = participantRepository.findRoomIdsByUserIds(participantIds);
            for (String roomId : existingRoomIds) {
                Long count = participantRepository.countParticipantsInRoom(roomId);
                if (count == 2) {
                    return chatRoomRepository.findById(roomId)
                            .orElseThrow(() -> new ChatException(ErrorCode.CHAT_ROOM_NOT_FOUND));
                }
            }
        }

        //새로운 채팅방 생성
        String roomId=UUID.randomUUID().toString();
        ChatRoom chatRoom=ChatRoom.builder().id(roomId).name(request.getName()).build();
        chatRoomRepository.save(chatRoom);

        //참여자 등록
        List<User> users= userRepository.findAllById(participantIds);
        for (User user:users) {
            participantRepository.save(ChatRoomParticipant.builder().chatRoom(chatRoom).user(user).build());
        }
        return chatRoom;
    }
    public List<ChatRoom> getAllRooms(Long userId) {
        return participantRepository.findRoomIdsByUserIds(Set.of(userId)).stream()
                .map(roomId->chatRoomRepository.findById(roomId)
                        .orElseThrow(()->new ChatException(ErrorCode.CHAT_ROOM_NOT_FOUND)))
                .toList();
    }

    public boolean isParticipant(String roomId, Long userId) {
        return participantRepository.existsByRoomIdAndUserId(roomId, userId);
    }

    @Transactional
    public void deleteRoom(String roomId, Long userId) {
        boolean isParticipant = participantRepository.existsByRoomIdAndUserId(roomId, userId);
        if (!isParticipant) {
            throw new ChatException(ErrorCode.FORBIDDEN_CHAT_DELETE);
        }
        participantRepository.deleteAllByChatRoomId(roomId);
        chatRoomRepository.deleteById(roomId);
    }
}