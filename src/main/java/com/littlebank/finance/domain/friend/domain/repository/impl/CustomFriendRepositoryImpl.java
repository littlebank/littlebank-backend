package com.littlebank.finance.domain.friend.domain.repository.impl;

import com.littlebank.finance.domain.friend.domain.QFriend;
import com.littlebank.finance.domain.friend.domain.repository.CustomFriendRepository;
import com.littlebank.finance.domain.friend.dto.response.FriendInfoResponse;
import com.littlebank.finance.domain.user.domain.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.littlebank.finance.domain.friend.domain.QFriend.friend;
import static com.littlebank.finance.domain.user.domain.QUser.user;

@RequiredArgsConstructor
public class CustomFriendRepositoryImpl implements CustomFriendRepository {
    private final JPAQueryFactory queryFactory;
    QUser u = user;
    QFriend f = friend;

    public Page<FriendInfoResponse> findFriendsByUserId(Long userId, Pageable pageable) {
        List<FriendInfoResponse> results =
                queryFactory.select(Projections.constructor(
                        FriendInfoResponse.class,
                        f.id,
                        f.customName,
                        Projections.constructor(
                                FriendInfoResponse.UserInfo.class,
                                u.id,
                                u.name,
                                u.profileImagePath,
                                u.role
                        )
                ))
                .from(f)
                .join(u).on(u.id.eq(f.toUser.id))
                .where(
                        f.fromUser.id.eq(userId),
                        JPAExpressions.selectOne()
                                .from(f)
                                .where(
                                        f.fromUser.id.eq(f.toUser.id),
                                        f.toUser.id.eq(userId),
                                        f.isBlocked.isTrue()
                                )
                                .notExists()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results, pageable, results.size());
    }
}
