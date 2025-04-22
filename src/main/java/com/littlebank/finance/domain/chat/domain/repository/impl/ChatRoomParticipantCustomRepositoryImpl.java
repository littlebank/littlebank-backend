package com.littlebank.finance.domain.chat.domain.repository.impl;

import com.littlebank.finance.domain.chat.domain.QChatRoomParticipant;
import com.littlebank.finance.domain.chat.domain.repository.ChatRoomParticipantCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class ChatRoomParticipantCustomRepositoryImpl implements ChatRoomParticipantCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<String> findRoomIdsByUserIds(Set<Long> userIds) {
        QChatRoomParticipant participant= QChatRoomParticipant.chatRoomParticipant;

        // 채팅방 참여자 테이블에서 주어진 사용자들이 모두 포함된 채팅방 roomId 조회
        return queryFactory.select(participant.chatRoom.id).from(participant)
                .where(participant.user.id.in(userIds))
                .groupBy(participant.chatRoom.id)
                .having(participant.user.count().eq((long) userIds.size()))
                .fetch();
    }

    @Override
    public Long countParticipantsInRoom(String roomId) {
        QChatRoomParticipant participant=QChatRoomParticipant.chatRoomParticipant;
        return queryFactory.select(participant.count())
                .from(participant).where(participant.chatRoom.id.eq(roomId))
                .fetchOne();
    }
}
