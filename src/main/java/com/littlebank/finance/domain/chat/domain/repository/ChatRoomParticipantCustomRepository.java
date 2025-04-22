package com.littlebank.finance.domain.chat.domain.repository;

import java.util.List;
import java.util.Set;
public interface ChatRoomParticipantCustomRepository {
    abstract List<String> findRoomIdsByUserIds(Set<Long> userIds);
    abstract Long countParticipantsInRoom(String roomId);
}
