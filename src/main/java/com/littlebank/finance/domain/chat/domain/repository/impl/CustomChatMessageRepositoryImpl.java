package com.littlebank.finance.domain.chat.domain.repository.impl;

import com.littlebank.finance.domain.chat.domain.QChatMessage;
import com.littlebank.finance.domain.chat.domain.QChatRoom;
import com.littlebank.finance.domain.chat.domain.QUserChatRoom;
import com.littlebank.finance.domain.chat.domain.UserChatRoom;
import com.littlebank.finance.domain.chat.domain.constant.RoomRange;
import com.littlebank.finance.domain.chat.domain.repository.CustomChatMessageRepository;
import com.littlebank.finance.domain.chat.dto.response.APIMessageResponse;
import com.littlebank.finance.domain.friend.domain.QFriend;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.littlebank.finance.domain.chat.domain.QChatMessage.chatMessage;
import static com.littlebank.finance.domain.chat.domain.QChatRoom.chatRoom;
import static com.littlebank.finance.domain.chat.domain.QUserChatRoom.userChatRoom;
import static com.littlebank.finance.domain.friend.domain.QFriend.friend;

@RequiredArgsConstructor
public class CustomChatMessageRepositoryImpl implements CustomChatMessageRepository {
    private final JPAQueryFactory queryFactory;
    private QChatRoom cr = chatRoom;
    private QUserChatRoom ucr = userChatRoom;
    private QChatMessage cm = chatMessage;
    private QFriend f = friend;

    /**
     * 채팅방을 나간 사람이 읽지 않은 메시지의 읽지 않음 갯수를 -1
     *
     * @param leaver 채팅방을 나간 사람의 정보
     */
    @Override
    public void decreaseReadCountByUserChatRoom(UserChatRoom leaver) {
        queryFactory
                .update(cm)
                .set(cm.readCount, cm.readCount.subtract(1))
                .where(
                        cm.room.id.eq(leaver.getRoom().getId()),
                        cm.id.gt(leaver.getLastReadMessageId()),
                        cm.id.loe(leaver.getRoom().getLastMessageId()),
                        cm.readCount.gt(0)
                )
                .execute();
    }

    /**
     * 채팅방 메시지를 최신순으로 페이징 조회
     *
     * - 메시지는 `lastMessageId`보다 작은 메시지부터 조회됨
     * - 1:1 채팅방(PRIVATE)의 경우, 차단한 상대의 메시지는 blockedDate 이전만 조회됨
     * - 그룹 채팅방(GROUP)의 경우, 차단 여부와 관계없이 모든 메시지를 조회함
     * - 본인이 보낸 메시지는 차단 여부에 관계없이 항상 조회됨
     *
     * @param userId        메시지를 조회하는 사용자 식별 id
     * @param roomId        채팅방 id
     * @param lastMessageId 페이징 기준이 되는 마지막 메시지 id
     * @param pageable      페이징 정보 (100개씩 조회)
     * @return 메시지 리스트 (최신순), Page 형태
     */
    @Override
    public Page<APIMessageResponse> findChatMessages(Long userId, Long roomId, Long lastMessageId, Pageable pageable) {
        RoomRange roomRange = queryFactory
                .select(cr.range)
                .from(cr)
                .where(cr.id.eq(roomId))
                .fetchOne();

        BooleanBuilder whereCondition = new BooleanBuilder()
                .and(cr.id.eq(roomId))
                .and(blockedMessageCondition(userId, roomRange))
                .and(cm.id.lt(lastMessageId))
                .and(cm.timestamp.goe(
                        JPAExpressions.select(ucr.createdDate)
                                .from(ucr)
                                .where(ucr.room.id.eq(roomId).and(ucr.user.id.eq(userId)))
                ));

        List<APIMessageResponse> results = queryFactory
                .select(Projections.constructor(
                        APIMessageResponse.class,
                        cm.id,
                        cm.content,
                        cm.messageType,
                        cm.timestamp,
                        cm.readCount,
                        cm.sender.id,
                        cm.sender.name,
                        cm.sender.profileImagePath,
                        f.isNotNull(),
                        f.customName,
                        f.isBestFriend,
                        f.isBlocked
                ))
                .from(cm)
                .join(cm.room, cr)
                .leftJoin(f).on(
                        f.fromUser.id.eq(userId)
                                .and(f.toUser.id.eq(cm.sender.id))
                )
                .where(whereCondition)
                .orderBy(cm.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory
                .select(cm.count())
                .from(cm)
                .join(cm.room, cr)
                .leftJoin(f).on(
                        f.fromUser.id.eq(userId)
                                .and(f.toUser.id.eq(cm.sender.id))
                )
                .where(whereCondition)
                .fetchOne();

        return new PageImpl<>(results, pageable, totalCount);
    }

    private BooleanExpression blockedMessageCondition(Long userId, RoomRange roomRange) {
        if (roomRange == RoomRange.GROUP) {
            return null;
        }

        BooleanExpression selfMessage = cm.sender.id.eq(userId);

        BooleanExpression notBlocked = f.isNull().or(f.isBlocked.isFalse());

        BooleanExpression blockedBeforeDate = f.isBlocked.isTrue()
                .and(f.blockedDate.isNotNull())
                .and(cm.timestamp.before(f.blockedDate));

        return selfMessage.or(notBlocked).or(blockedBeforeDate);
    }
}

