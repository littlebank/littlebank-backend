package com.littlebank.finance.domain.friend.domain.repository.impl;

import com.littlebank.finance.domain.friend.domain.FriendSearchHistory;
import com.littlebank.finance.domain.friend.domain.QFriend;
import com.littlebank.finance.domain.friend.domain.QFriendSearchHistory;
import com.littlebank.finance.domain.friend.domain.repository.CustomFriendSearchHistoryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.littlebank.finance.domain.friend.domain.QFriend.friend;
import static com.littlebank.finance.domain.friend.domain.QFriendSearchHistory.friendSearchHistory;

@RequiredArgsConstructor
public class CustomFriendSearchHistoryRepositoryImpl implements CustomFriendSearchHistoryRepository {
    private final JPAQueryFactory queryFactory;
    private QFriendSearchHistory fsh = friendSearchHistory;
    private QFriend f = friend;

    @Override
    public void deleteSearchHistoryInIds(Long userId, List<Long> searchHistoryIds) {
        queryFactory.delete(fsh)
                .where(
                        fsh.user.id.eq(userId),
                        fsh.id.in(searchHistoryIds)
                )
                .execute();
    }

    @Override
    public List<FriendSearchHistory> findByUserIdFetchKoinFriend(Long userId) {
        return queryFactory.selectFrom(fsh)
                .join(f).on(f.id.eq(fsh.friend.id))
                .where(
                        fsh.user.id.eq(userId)
                )
                .orderBy(fsh.searchAt.desc())
                .fetch();
    }
}
