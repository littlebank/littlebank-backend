package com.littlebank.finance.domain.chat.controller;

import com.littlebank.finance.domain.chat.domain.ChatMessage;
import com.littlebank.finance.domain.chat.domain.RoomRange;
import com.littlebank.finance.domain.chat.domain.UserChatRoom;
import com.littlebank.finance.domain.chat.dto.request.ChatMessageRequest;
import com.littlebank.finance.domain.chat.dto.response.ChatMessageResponse;
import com.littlebank.finance.domain.chat.service.ChatMessageService;
import com.littlebank.finance.domain.friend.domain.Friend;
import com.littlebank.finance.domain.friend.service.FriendService;
import com.littlebank.finance.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatMessageController {
    private final static String CHAT_SUBSCRIBE_BASE_URL = "/sub/chat/";
    private final ChatMessageService chatMessageService;
    private final FriendService friendService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat-send")
    public void sendMessage(@Payload ChatMessageRequest request, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        ChatMessage savedMessage = chatMessageService.saveMessage(customUserDetails.getId(), request);

        List<UserChatRoom> participants = chatMessageService.getChatRoomParticipants(request.getRoomId());
        for (UserChatRoom participant : participants) {
            Long receiverId = participant.getUser().getId();
            if (receiverId.equals(customUserDetails.getId()))
                continue;

            Friend friend = friendService.findFriend(receiverId, customUserDetails.getId());
            if (participant.getRoom().getRange() == RoomRange.PRIVATE &&
                    friend != null && friend.getIsBlocked())
                continue;

            ChatMessageResponse response = ChatMessageResponse.of(participant, savedMessage, friend);

            messagingTemplate.convertAndSend(
                    CHAT_SUBSCRIBE_BASE_URL + request.getRoomId() + "/" + receiverId,
                    response
            );
        }
    }

}
