package com.littlebank.finance.domain.relationship.domain.repository;

import com.littlebank.finance.domain.relationship.domain.RelationshipType;
import com.littlebank.finance.domain.relationship.dto.response.RelationshipRequestsReceivedResponse;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.dto.response.UserSearchResponse;

import java.util.List;
import java.util.Optional;

public interface CustomRelationshipRepository {
    boolean existsSameTypeBetweenUsers(Long userAId, Long userBId, RelationshipType relationshipType);
    Optional<UserSearchResponse> findUserSearchResponse(Long requesterId, User searchUser);
    List<RelationshipRequestsReceivedResponse> findRequestsReceived(Long fromUserId);
}
