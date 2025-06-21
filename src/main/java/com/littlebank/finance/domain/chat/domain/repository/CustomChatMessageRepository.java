package com.littlebank.finance.domain.chat.domain.repository;

import com.littlebank.finance.domain.chat.domain.UserChatRoom;
import com.littlebank.finance.domain.chat.dto.response.APIMessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomChatMessageRepository {
    void decreaseReadCountByUserChatRoom(UserChatRoom leaver);

    Page<APIMessageResponse> findChatMessages(Long userId, Long roomId, Long lastMessageId, Pageable pageable);
}
