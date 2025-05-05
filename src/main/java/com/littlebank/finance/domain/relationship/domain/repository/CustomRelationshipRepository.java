package com.littlebank.finance.domain.relationship.domain.repository;

import com.littlebank.finance.domain.relationship.domain.RelationshipType;
import com.littlebank.finance.domain.relationship.dto.response.RelationshipRequestsReceivedResponse;

import java.util.List;

public interface CustomRelationshipRepository {
    boolean existsSameTypeBetweenUsers(Long userAId, Long userBId, RelationshipType relationshipType);
    List<RelationshipRequestsReceivedResponse> findRequestsReceived(Long fromUserId);
}
