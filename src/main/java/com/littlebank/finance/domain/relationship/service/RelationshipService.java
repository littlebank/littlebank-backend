package com.littlebank.finance.domain.relationship.service;

import com.littlebank.finance.domain.relationship.domain.Relationship;
import com.littlebank.finance.domain.relationship.domain.RelationshipStatus;
import com.littlebank.finance.domain.relationship.domain.RelationshipType;
import com.littlebank.finance.domain.relationship.domain.repository.RelationshipRepository;
import com.littlebank.finance.domain.relationship.dto.request.RelationshipRequest;
import com.littlebank.finance.domain.relationship.dto.response.RelationshipResponse;
import com.littlebank.finance.domain.relationship.exception.RelationshipException;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RelationshipService {
    private final UserRepository userRepository;
    private final RelationshipRepository relationshipRepository;

    public RelationshipResponse createRelationship(RelationshipRequest request, long userId) {
        User fromUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        User toUser = userRepository.findById(request.getTargetUserId())
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        verifyExistsRelationship(fromUser, toUser, request.getRelationshipType());

        Relationship relationshipByFromUser = relationshipRepository.save(
                Relationship.builder()
                        .customName(toUser.getName())
                        .fromUser(fromUser)
                        .toUser(toUser)
                        .relationshipType(request.getRelationshipType())
                        .relationshipStatus(RelationshipStatus.REQUESTED)
                        .build()
        );

        relationshipRepository.save(
                Relationship.builder()
                        .customName(fromUser.getName())
                        .fromUser(toUser)
                        .toUser(fromUser)
                        .relationshipType(request.getRelationshipType())
                        .relationshipStatus(RelationshipStatus.REQUESTED_BY_OTHER)
                        .build()
        );

        return RelationshipResponse.of(relationshipByFromUser);
    }

    private void verifyExistsRelationship(User fromUser, User toUser, RelationshipType relationshipType) {
        if (relationshipRepository.existsSameTypeBetweenUsers(
                fromUser.getId(), toUser.getId(), relationshipType
        )) {
            throw new RelationshipException(ErrorCode.ALREADY_RELATIONSHIP_EXISTS);
        }
    }
}
