package com.littlebank.finance.domain.friend.domain.repository.impl;

import com.littlebank.finance.domain.chat.dto.UserFriendInfoDto;
import com.littlebank.finance.domain.friend.domain.QFriend;
import com.littlebank.finance.domain.friend.domain.repository.CustomFriendRepository;
import com.littlebank.finance.domain.friend.dto.response.CommonFriendInfoResponse;
import com.littlebank.finance.domain.friend.dto.response.FriendInfoResponse;
import com.littlebank.finance.domain.mission.domain.QMission;
import com.littlebank.finance.domain.user.domain.QUser;
import com.littlebank.finance.domain.user.dto.response.CommonUserInfoResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

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

    /**
     * 내 이름 'name'을 업데이틀할 때
     * 나를 친구로 등록한 유저에게 표시되는 내 이름 'customName'이 'name'과 일치할 때
     * 'customName'을 'name'으로 업데이트
     *
     * @param toUserId
     * @param beforeName
     * @param afterName
     */
    @Override
    public void updateCustomName(Long toUserId, String beforeName, String afterName) {
        queryFactory
                .update(f)
                .set(f.customName, afterName)
                .where(
                        f.toUser.id.eq(toUserId),
                        f.customName.eq(beforeName)
                )
                .execute();
    }

    /**
     * 친구 목록을 조회
     *
     * @param userId 유저 식별 id
     * @return
     */
    @Override
    public List<FriendInfoResponse> findFriendsByUserId(Long userId) {
        List<FriendInfoResponse> results =
                queryFactory.select(Projections.constructor(
                                FriendInfoResponse.class,
                                Projections.constructor(
                                        CommonUserInfoResponse.class,
                                        u.id,
                                        u.name,
                                        u.rrn,
                                        u.phone,
                                        u.statusMessage,
                                        u.bankName,
                                        u.bankCode,
                                        u.bankAccount,
                                        u.profileImagePath,
                                        u.role
                                ),
                                Projections.constructor(
                                        CommonFriendInfoResponse.class,
                                        f.id.isNotNull(),
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
                        .fetch();

        return results;
    }

    /**
     * 나를 친구로 추가한 유저를 조회
     *
     * @param userId '나'의 유저 식별 id (본인)
     * @return
     */
    @Override
    public List<FriendInfoResponse> findFriendAddedMeByUserId(Long userId) {
        List<FriendInfoResponse> results =
                queryFactory.select(Projections.constructor(
                                FriendInfoResponse.class,
                                Projections.constructor(
                                        CommonUserInfoResponse.class,
                                        u.id,
                                        u.name,
                                        u.rrn,
                                        u.phone,
                                        u.statusMessage,
                                        u.bankName,
                                        u.bankCode,
                                        u.bankAccount,
                                        u.profileImagePath,
                                        u.role
                                ),
                                Projections.constructor(
                                        CommonFriendInfoResponse.class,
                                        f1.id.isNotNull(),
                                        f1.id,
                                        f1.customName,
                                        f1.isBlocked,
                                        f1.isBestFriend
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
                        .fetch();

        return results;
    }

    /**
     * 자신이 설정한 친구의 이름(customName)에 주어진 키워드가 포함된 친구들을 검색
     * 나를 차단한 친구는 검색 결과에서 제외
     * 대소문자를 구분하지 않고 검색하며, 친구 정보와 사용자 정보를 함께 반환
     *
     * @param userId  검색을 수행할 기준 사용자 식별 id
     * @param keyword 친구의 사용자 정의 이름에서 검색할 키워드 (대소문자 구분 없음)
     * @return {@link FriendInfoResponse} 객체 리스트 (사용자 정보 + 친구 관계 정보 포함)
     */
    @Override
    public List<FriendInfoResponse> searchFriendsByKeyword(Long userId, String keyword) {
        return queryFactory.select(Projections.constructor(
                        FriendInfoResponse.class,
                        Projections.constructor(
                                CommonUserInfoResponse.class,
                                f.toUser.id,
                                f.toUser.name,
                                f.toUser.rrn,
                                f.toUser.phone,
                                f.toUser.statusMessage,
                                f.toUser.bankName,
                                f.toUser.bankCode,
                                f.toUser.bankAccount,
                                f.toUser.profileImagePath,
                                f.toUser.role
                        ),
                        Projections.constructor(
                                CommonFriendInfoResponse.class,
                                f1.id.isNotNull(),
                                f.id,
                                f.customName,
                                f.isBlocked,
                                f.isBestFriend
                        )
                ))
                .from(f)
                .where(
                        f.fromUser.id.eq(userId),
                        f.customName.containsIgnoreCase(keyword),
                        JPAExpressions.selectOne()
                                .from(f1)
                                .where(
                                        f1.toUser.id.eq(f.fromUser.id),
                                        f1.fromUser.id.eq(f.toUser.id),
                                        f1.isBlocked.isTrue()
                                )
                                .notExists()
                )
                .fetch();
    }

    /**
     * 기준 사용자와 여러 대상 사용자들 간의 친구 관계를 조회
     *
     * @param fromUserId    친구를 조회하는 기준 사용자 식별 id (ex. 본인)
     * @param toUserId     친구 대상 사용자 식별 id
     * @return FriendDto 리스트로 매핑된 친구 정보 목록
     */
    @Override
    public Optional<UserFriendInfoDto> findUserFriendInfoDto(Long fromUserId, Long toUserId) {
        return Optional.ofNullable(
                queryFactory.select(Projections.constructor(
                                UserFriendInfoDto.class,
                                u.id,
                                u.name,
                                u.profileImagePath,
                                new CaseBuilder()
                                        .when(f.id.isNotNull()).then(true)
                                        .otherwise(false),
                                f.id,
                                f.customName,
                                f.isBlocked,
                                f.isBestFriend
                        ))
                        .from(u)
                        .leftJoin(f).on(
                                f.toUser.id.eq(u.id)
                                        .and(f.fromUser.id.eq(fromUserId))
                        )
                        .where(u.id.eq(toUserId))
                        .fetchOne()
        );
    }

    /**
     * 기준 사용자와 여러 대상 사용자들 간의 친구 관계 목록을 조회
     *
     * @param fromUserId    친구를 조회하는 기준 사용자 식별 id (ex. 본인)
     * @param toUserIds     친구 대상 사용자 식별 id 목록
     * @return FriendDto 리스트로 매핑된 친구 정보 목록
     */
    @Override
    public List<UserFriendInfoDto> findUserFriendInfoDtoList(Long fromUserId, List<Long> toUserIds) {
        return queryFactory.select(Projections.constructor(
                        UserFriendInfoDto.class,
                        u.id,
                        u.name,
                        u.profileImagePath,
                        new CaseBuilder()
                                .when(f.id.isNotNull()).then(true)
                                .otherwise(false),
                        f.id,
                        f.customName,
                        f.isBlocked,
                        f.isBestFriend
                ))
                .from(u)
                .leftJoin(f).on(
                        f.toUser.id.eq(u.id)
                                .and(f.fromUser.id.eq(fromUserId))
                )
                .where(u.id.in(toUserIds))
                .fetch();
    }

}
