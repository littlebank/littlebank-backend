package com.littlebank.finance.domain.relationship.domain.repository.impl;

import com.littlebank.finance.domain.relationship.domain.*;
import com.littlebank.finance.domain.relationship.domain.repository.CustomRelationshipRepository;
import com.littlebank.finance.domain.relationship.dto.response.RelationshipRequestsReceivedResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.littlebank.finance.domain.relationship.domain.QCustomNameMapping.customNameMapping;
import static com.littlebank.finance.domain.relationship.domain.QRelationship.relationship;

@RequiredArgsConstructor
public class CustomRelationshipRepositoryImpl implements CustomRelationshipRepository {
    private final JPAQueryFactory queryFactory;
    private final QRelationship r = relationship;
    private final QCustomNameMapping cnm = customNameMapping;

    @Override
    public boolean existsSameTypeBetweenUsers(Long userAId, Long userBId, RelationshipType relationshipType) {
        Integer result = queryFactory
                .selectOne()
                .from(r)
                .where(
                        r.relationshipType.eq(relationshipType)
                                .and(
                                        r.fromUser.id.eq(userAId).and(r.toUser.id.eq(userBId))
                                )
                )
                .fetchFirst();

        return result != null;
    }

    @Override
    public List<RelationshipRequestsReceivedResponse> findRequestsReceived(Long fromUserId) {
        return queryFactory
                .select(Projections.constructor(
                        RelationshipRequestsReceivedResponse.class,
                        Projections.constructor(
                                RelationshipRequestsReceivedResponse.UserInfo.class,
                                r.toUser.id,
                                cnm.customName,
                                r.toUser.profileImagePath,
                                r.toUser.role
                        ),
                        Projections.constructor(
                                RelationshipRequestsReceivedResponse.RelationInfo.class,
                                r.id,
                                r.relationshipType,
                                r.relationshipStatus,
                                r.createdDate
                        )
                ))
                .from(r)
                .innerJoin(cnm).on(
                        cnm.fromUser.id.eq(fromUserId)
                                .and(cnm.toUser.id.eq(r.toUser.id))
                )
                .where(
                        r.fromUser.id.eq(fromUserId)
                                .and(r.relationshipStatus.eq(RelationshipStatus.REQUESTED_BY_OTHER))
                )
                .fetch();
    }
}
