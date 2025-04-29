package com.littlebank.finance.domain.relationship.domain.repository;

import com.littlebank.finance.domain.relationship.domain.RelationshipType;

public interface CustomRelationshipRepository {
    boolean existsSameTypeBetweenUsers(Long userAId, Long userBId, RelationshipType relationshipType);
}
