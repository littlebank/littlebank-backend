package com.littlebank.finance.domain.chat.domain.repository.impl;

import com.littlebank.finance.domain.chat.domain.QChatRoom;
import com.littlebank.finance.domain.chat.domain.QUserChatRoom;
import com.littlebank.finance.domain.chat.domain.RoomRange;
import com.littlebank.finance.domain.chat.domain.RoomType;
import com.littlebank.finance.domain.chat.domain.repository.CustomUserChatRoomRepository;
import com.littlebank.finance.domain.chat.dto.response.ChatRoomSummaryResponse;
import com.littlebank.finance.domain.friend.domain.QFriend;
import com.littlebank.finance.domain.user.domain.QUser;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.littlebank.finance.domain.chat.domain.QChatRoom.chatRoom;
import static com.littlebank.finance.domain.chat.domain.QUserChatRoom.userChatRoom;
import static com.littlebank.finance.domain.friend.domain.QFriend.friend;
import static com.littlebank.finance.domain.user.domain.QUser.user;

@RequiredArgsConstructor
public class CustomUserChatRoomRepositoryImpl implements CustomUserChatRoomRepository {
    private final JPAQueryFactory queryFactory;
    private QUser u = user;
    private QChatRoom ch = chatRoom;
    private QUserChatRoom ucr = userChatRoom;
    private QFriend f = friend;


    /**
     * 참여 중인 친구 채팅방 목록을 조회
     *
     * 각 채팅방에 대해 다음 정보를 제공
     * - userChatRoomId: 사용자-채팅방 매핑 ID
     * - roomId: 채팅방 ID
     * - roomName: 채팅방 이름
     * - roomType: 채팅방 타입 (FRIEND로 한정)
     * - roomRange: 채팅방 공개 범위
     * - displayIdx: 사용자의 채팅방 정렬 기준 시간
     * - participantNameList: 채팅방 참여자 이름 목록 (본인 제외)
     *      - 친구 관계가 있는 경우: Friend.customName 사용
     *      - 친구가 아닌 경우: User.name 사용
     *
     * 조건:
     * - 채팅방 타입이 FRIEND인 경우만 조회
     * - 삭제되지 않은 채팅방(User, ChatRoom 엔티티의 @Where 조건 포함)이 기본적으로 필터링됨
     *
     * @param userId 조회할 사용자의 ID
     * @return 채팅방 요약 정보 리스트
     */
    @Override
    public List<ChatRoomSummaryResponse> findChatRoomSummaryList(Long userId) {
        System.out.println("userId : " + userId);
        List<Tuple> myRooms = queryFactory
                .select(ucr.id, ch.id, ch.name, ch.type, ch.range, ucr.displayIdx)
                .from(ucr)
                .join(ucr.room, ch)
                .where(
                        ucr.user.id.eq(userId),
                        ch.type.eq(RoomType.FRIEND)
                )
                .fetch();

        return myRooms.stream().map(tuple -> {
            Long userChatRoomId = tuple.get(ucr.id);
            Long roomId = tuple.get(ch.id);
            String roomName = tuple.get(ch.name);
            RoomType roomType = tuple.get(ch.type);
            RoomRange roomRange = tuple.get(ch.range);
            LocalDateTime displayIdx = tuple.get(ucr.displayIdx);
            System.out.println("확인 : " + userChatRoomId);

            // 참여자 이름 목록 (본인 제외 + 친구면 customName)
            List<String> participantNames = queryFactory
                    .select(
                            new CaseBuilder()
                                    .when(friend.id.isNotNull())
                                    .then(friend.customName)
                                    .otherwise(u.name)
                    )
                    .from(ucr)
                    .join(ucr.user, u)
                    .leftJoin(friend)
                    .on(friend.fromUser.id.eq(userId)
                            .and(friend.toUser.id.eq(u.id)))
                    .where(ucr.room.id.eq(roomId)
                            .and(u.id.ne(userId))) // 본인 제외
                    .fetch();

            return new ChatRoomSummaryResponse(
                    userChatRoomId,
                    roomId,
                    roomName,
                    roomType,
                    roomRange,
                    participantNames,
                    displayIdx
            );
        }).collect(Collectors.toList());
    }
}
