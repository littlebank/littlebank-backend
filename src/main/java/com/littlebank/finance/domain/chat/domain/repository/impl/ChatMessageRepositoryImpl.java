package com.littlebank.finance.domain.chat.domain.repository.impl;

import com.littlebank.finance.domain.chat.domain.QUserChatRoom;
import com.littlebank.finance.domain.chat.domain.repository.CustomChatMessageRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static com.littlebank.finance.domain.chat.domain.QUserChatRoom.userChatRoom;

@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements CustomChatMessageRepository {
    private final JPAQueryFactory queryFactory;
    private QUserChatRoom ucr = userChatRoom;

    /**
     * 메시지 전송 시 displayIdx를 현재 시간으로 update, 벌크 연산 수행
     *
     * @param roomId 업데이트할 채팅방 식별 id
     */
    @Override
    public void updateDisplayIdxByRoomId(Long roomId) {
        queryFactory
                .update(ucr)
                .set(ucr.displayIdx, LocalDateTime.now())
                .where(ucr.room.id.eq(roomId))
                .execute();
    }
}
