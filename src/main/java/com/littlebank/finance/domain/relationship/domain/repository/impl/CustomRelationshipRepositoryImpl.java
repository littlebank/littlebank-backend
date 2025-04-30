package com.littlebank.finance.domain.relationship.domain.repository.impl;

import com.littlebank.finance.domain.relationship.domain.QCustomNameMapping;
import com.littlebank.finance.domain.relationship.domain.QRelationship;
import com.littlebank.finance.domain.relationship.domain.RelationshipType;
import com.littlebank.finance.domain.relationship.domain.repository.CustomRelationshipRepository;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.dto.response.UserSearchResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.littlebank.finance.domain.relationship.domain.QRelationship.relationship;

@RequiredArgsConstructor
public class CustomRelationshipRepositoryImpl implements CustomRelationshipRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsSameTypeBetweenUsers(Long userAId, Long userBId, RelationshipType relationshipType) {
        QRelationship r = relationship;

        Integer result = queryFactory
                .selectOne()
                .from(r)
                .where(
                        r.relationshipType.eq(relationshipType)
                                .and(
                                        r.fromUser.id.eq(userAId).and(r.toUser.id.eq(userBId))
                                ),
                        r.isDeleted.isFalse()
                )
                .fetchFirst();

        return result != null;
    }

    public Optional<UserSearchResponse> findUserSearchResponse(Long requesterId, User searchUser) {
        QRelationship relationship = QRelationship.relationship;
        QCustomNameMapping cnm = QCustomNameMapping.customNameMapping;

        List<UserSearchResponse.RelationResponse> relations = queryFactory
                .select(
                        Projections.constructor(UserSearchResponse.RelationResponse.class,
                                cnm.customName,
                                relationship.relationshipType,
                                relationship.relationshipStatus
                        )
                )
                .from(relationship)
                .innerJoin(cnm).on(
                        cnm.fromUser.id.eq(requesterId)
                                .and(cnm.toUser.id.eq(relationship.toUser.id))
                )
                .where(
                        relationship.fromUser.id.eq(requesterId)
                                .and(relationship.toUser.id.eq(searchUser.getId()))
                )
                .fetch();

        return Optional.ofNullable(
                UserSearchResponse.builder()
                .searchUserId(searchUser.getId())
                .email(searchUser.getEmail())
                .name(searchUser.getName())
                .profileImagePath(searchUser.getProfileImagePath())
                .role(searchUser.getRole())
                .relationships(relations)
                .build()
        );
    }
}
