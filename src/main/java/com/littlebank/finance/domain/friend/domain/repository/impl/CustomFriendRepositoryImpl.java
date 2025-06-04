package com.littlebank.finance.domain.friend.domain.repository.impl;

import com.littlebank.finance.domain.friend.domain.QFriend;
import com.littlebank.finance.domain.friend.domain.repository.CustomFriendRepository;
import com.littlebank.finance.domain.friend.dto.response.CommonFriendInfoResponse;
import com.littlebank.finance.domain.friend.dto.response.FriendInfoResponse;
import com.littlebank.finance.domain.mission.domain.QMission;
import com.littlebank.finance.domain.user.domain.QUser;
import com.littlebank.finance.domain.user.dto.response.CommonUserInfoResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.littlebank.finance.domain.friend.domain.QFriend.friend;
import static com.littlebank.finance.domain.mission.domain.QMission.mission;
import static com.littlebank.finance.domain.user.domain.QUser.user;

@RequiredArgsConstructor
public class CustomFriendRepositoryImpl implements CustomFriendRepository {
    private final JPAQueryFactory queryFactory;
    private QUser u = user;
    private QFriend f = friend;
    private QFriend f1 = new QFriend("f1");
    private QMission m = mission;
    @Override
    public Page<FriendInfoResponse> findFriendsByUserId(Long userId, Pageable pageable) {
        List<FriendInfoResponse> results =
                queryFactory.select(Projections.constructor(
                                FriendInfoResponse.class,
                                Projections.constructor(
                                        CommonUserInfoResponse.class,
                                        u.id,
                                        u.name,
                                        u.rrn,
                                        u.statusMessage,
                                        u.bankName,
                                        u.bankCode,
                                        u.bankAccount,
                                        u.profileImagePath,
                                        u.role
                                ),
                                Projections.constructor(
                                        CommonFriendInfoResponse.class,
                                        f.id,
                                        f.customName,
                                        f.isBlocked,
                                        f.isBestFriend
                                )
                        ))
                        .from(f)
                        .join(u).on(u.id.eq(f.toUser.id))
                        .where(
                                f.fromUser.id.eq(userId),
                                JPAExpressions.selectOne()
                                        .from(f1)
                                        .where(
                                                f1.fromUser.id.eq(f.toUser.id),
                                                f1.toUser.id.eq(userId),
                                                f1.isBlocked.isTrue()
                                        )
                                        .notExists()
                        )
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        return new PageImpl<>(results, pageable, results.size());
    }

    @Override
    public Page<FriendInfoResponse> findFriendAddedMeByUserId(Long userId, Pageable pageable) {
        List<FriendInfoResponse> results =
                queryFactory.select(Projections.constructor(
                                FriendInfoResponse.class,
                                Projections.constructor(
                                        CommonUserInfoResponse.class,
                                        u.id,
                                        u.name,
                                        u.rrn,
                                        u.statusMessage,
                                        u.bankName,
                                        u.bankCode,
                                        u.bankAccount,
                                        u.profileImagePath,
                                        u.role
                                ),
                                Projections.constructor(
                                        CommonFriendInfoResponse.class,
                                        f.id,
                                        f.customName,
                                        f.isBlocked,
                                        f.isBestFriend
                                )
                        ))
                        .from(f)
                        .join(u).on(u.id.eq(f.fromUser.id))
                        .leftJoin(f1).on(
                                f1.toUser.id.eq(f.fromUser.id)
                                        .and(f1.fromUser.id.eq(f.toUser.id))
                        )
                        .where(
                                f.toUser.id.eq(userId),
                                f.isBlocked.isFalse()
                        )
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        return new PageImpl<>(results, pageable, results.size());
    }
    public List<FriendInfoResponse> findFriendsByUserId(Long userId) {
        return queryFactory.select(Projections.constructor(
                        FriendInfoResponse.class,
                        Projections.constructor(
                                CommonUserInfoResponse.class,
                                u.id.as("userId"),
                                u.name.as("realName"),
                                u.rrn,
                                u.statusMessage,
                                u.profileImagePath,
                                u.role
                        ),
                        Projections.constructor(
                                CommonFriendInfoResponse.class,
                                f.id,
                                f.customName,
                                f.isBlocked,
                                f.isBestFriend
                        )
                ))
                .from(f)
                .join(u).on(u.id.eq(f.toUser.id))
                .where (
                        f.fromUser.id.eq(userId),
                        JPAExpressions.selectOne()
                                .from(f1)
                                .where(
                                        f1.fromUser.id.eq(f.toUser.id),
                                        f1.toUser.id.eq(userId),
                                        f1.isBlocked.isTrue()
                                )
                                .notExists()
                )
                .fetch();
    }
}
