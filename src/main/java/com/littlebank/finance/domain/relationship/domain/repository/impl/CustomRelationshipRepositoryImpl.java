package com.littlebank.finance.domain.relationship.domain.repository.impl;

import com.littlebank.finance.domain.relationship.domain.QRelationship;
import com.littlebank.finance.domain.relationship.domain.RelationshipType;
import com.littlebank.finance.domain.relationship.domain.repository.CustomRelationshipRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

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
}
