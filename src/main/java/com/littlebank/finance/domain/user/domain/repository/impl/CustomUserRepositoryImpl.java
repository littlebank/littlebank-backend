package com.littlebank.finance.domain.user.domain.repository.impl;

import com.littlebank.finance.domain.relationship.domain.QCustomNameMapping;
import com.littlebank.finance.domain.relationship.domain.QRelationship;
import com.littlebank.finance.domain.user.domain.QUser;
import com.littlebank.finance.domain.user.domain.repository.CustomUserRepository;
import com.littlebank.finance.domain.user.dto.response.UserDetailsInfoResponse;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.error.exception.ErrorCode;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.littlebank.finance.domain.relationship.domain.QCustomNameMapping.customNameMapping;
import static com.littlebank.finance.domain.relationship.domain.QRelationship.relationship;
import static com.littlebank.finance.domain.user.domain.QUser.user;

@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository {
    private final JPAQueryFactory queryFactory;
    private final QUser u = user;
    private final QRelationship r = relationship;
    private final QCustomNameMapping cnm = customNameMapping;

    @Override
    public Optional<UserDetailsInfoResponse> findUserDetailsInfo(Long searchUserId, Long userId) {
        Tuple userTuple = queryFactory
                .select(u.id, u.email, u.name, cnm.customName, u.phone, u.rrn,
                        u.bankName, u.bankCode, u.bankAccount, u.profileImagePath, u.role)
                .from(u)
                .leftJoin(cnm)
                .on(cnm.fromUser.id.eq(userId).and(cnm.toUser.id.eq(searchUserId)))
                .where(u.id.eq(searchUserId))
                .fetchOne();

        if (userTuple == null) {
            throw new UserException(ErrorCode.USER_NOT_FOUND);
        }

        List<UserDetailsInfoResponse.RelationInfo> relationList = queryFactory
                .select(Projections.constructor(
                        UserDetailsInfoResponse.RelationInfo.class,
                        r.id,
                        r.relationshipType,
                        r.relationshipStatus,
                        r.createdDate
                ))
                .from(r)
                .where(
                        r.fromUser.id.eq(userId)
                                .and(r.toUser.id.eq(searchUserId))
                )
                .fetch();

        return Optional.of(
                UserDetailsInfoResponse.builder()
                        .userId(userTuple.get(u.id))
                        .email(userTuple.get(u.email))
                        .realName(userTuple.get(u.name))
                        .customName(userTuple.get(cnm.customName))
                        .phone(userTuple.get(u.phone))
                        .rrn(userTuple.get(u.rrn))
                        .bankName(userTuple.get(u.bankName))
                        .bankCode(userTuple.get(u.bankCode))
                        .bankAccount(userTuple.get(u.bankAccount))
                        .profileImagePath(userTuple.get(u.profileImagePath))
                        .role(userTuple.get(u.role))
                        .relation(relationList)
                        .build()
        );
    }
}
