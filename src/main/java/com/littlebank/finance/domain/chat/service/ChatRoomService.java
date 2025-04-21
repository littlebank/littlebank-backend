package com.littlebank.finance.domain.chat.service;

import com.littlebank.finance.domain.chat.dto.request.ChatRoomRequest;
import com.littlebank.finance.domain.chat.domain.ChatRoom;
import com.littlebank.finance.domain.chat.domain.ChatRoomParticipant;
import com.littlebank.finance.domain.chat.domain.repository.ChatRoomParticipantRepository;
import com.littlebank.finance.domain.chat.domain.repository.ChatRoomRepository;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.global.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomParticipantRepository participantRepository;
    private final UserRepository userRepository;

    public ChatRoom createRoom(ChatRoomRequest request) {
        String roomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.builder().id(roomId).name(request.getName()).build();
        chatRoomRepository.save(chatRoom);

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentUserId = userDetails.getId();

        Set<Long> allParticipantIds = new HashSet<>(request.getParticipantIds());
        allParticipantIds.add(currentUserId);

        List<User> users = userRepository.findAllById(allParticipantIds.stream().toList());
        for (User user : users) {
            participantRepository.save(ChatRoomParticipant.builder()
                    .chatRoom(chatRoom)
                    .user(user)
                    .build());
        }

        return chatRoom;
    }

    public List<ChatRoom> getAllRooms() {
        return chatRoomRepository.findAll();
    }

    public boolean isParticipant(String roomId, String userId) {
        return participantRepository.existsByRoomIdAndUserId(roomId, userId);
    }

    @Transactional
    public void deleteRoom(String roomId) {
        participantRepository.deleteAllByChatRoomId(roomId);
        chatRoomRepository.deleteById(roomId);
    }
}