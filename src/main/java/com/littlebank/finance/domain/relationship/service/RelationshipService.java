package com.littlebank.finance.domain.relationship.service;

import com.littlebank.finance.domain.relationship.domain.CustomNameMapping;
import com.littlebank.finance.domain.relationship.domain.Relationship;
import com.littlebank.finance.domain.relationship.domain.RelationshipStatus;
import com.littlebank.finance.domain.relationship.domain.RelationshipType;
import com.littlebank.finance.domain.relationship.domain.repository.CustomNameMappingRepository;
import com.littlebank.finance.domain.relationship.domain.repository.RelationshipRepository;
import com.littlebank.finance.domain.relationship.dto.request.RelationshipReqAcceptRequest;
import com.littlebank.finance.domain.relationship.dto.request.RelationshipRequest;
import com.littlebank.finance.domain.relationship.dto.response.RelationshipReqAcceptResponse;
import com.littlebank.finance.domain.relationship.dto.response.RelationshipRequestsReceivedResponse;
import com.littlebank.finance.domain.relationship.dto.response.RelationshipResponse;
import com.littlebank.finance.domain.relationship.exception.RelationshipException;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RelationshipService {
    private final UserRepository userRepository;
    private final RelationshipRepository relationshipRepository;
    private final CustomNameMappingRepository customNameMappingRepository;

    public RelationshipResponse createRelationship(RelationshipRequest request, Long userId) {
        User fromUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        User toUser = userRepository.findById(request.getTargetUserId())
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        verifyExistsRelationship(fromUser, toUser, request.getRelationshipType());

        Relationship relationshipByFromUser = saveRelationship(fromUser, toUser, request.getRelationshipType(), RelationshipStatus.REQUESTED);
        if (!customNameMappingRepository.existsByFromUserIdAndToUserId(fromUser.getId(), toUser.getId())) {
            saveCustomName(fromUser, toUser);
        }

        saveRelationship(toUser, fromUser, request.getRelationshipType(), RelationshipStatus.REQUESTED_BY_OTHER);
        if (!customNameMappingRepository.existsByFromUserIdAndToUserId(toUser.getId(), fromUser.getId())) {
            saveCustomName(toUser, fromUser);
        }

        return RelationshipResponse.of(relationshipByFromUser);
    }

    @Transactional(readOnly = true)
    public List<RelationshipRequestsReceivedResponse> getReceivedRelationshipRequests(Long userId) {
        return relationshipRepository.findRequestsReceived(userId);
    }

    public RelationshipReqAcceptResponse acceptRelationshipRequest(RelationshipReqAcceptRequest request, Long userId) {
        Relationship relationship = relationshipRepository.findById(request.getRelationshipId())
                .orElseThrow(() -> new RelationshipException(ErrorCode.RELATIONSHIP_NOT_FOUND));
        Relationship oppositeRelationship = relationshipRepository.findByFromUserIdAndToUserIdAndRelationshipType(relationship.getToUser().getId(), userId, relationship.getRelationshipType())
                .orElseThrow(() -> new RelationshipException(ErrorCode.RELATIONSHIP_NOT_FOUND));
        relationship.updateStatusByConnection();
        oppositeRelationship.updateStatusByConnection();

        return RelationshipReqAcceptResponse.of(relationship);
    }

    public void deleteRelationship(Long relationshipId) {
        Relationship relationship = relationshipRepository.findById(relationshipId)
                        .orElseThrow(() -> new RelationshipException(ErrorCode.RELATIONSHIP_NOT_FOUND));
        relationshipRepository.deleteById(relationship.getId());
    }

    private Relationship saveRelationship(
            User from, User to, RelationshipType relationshipType, RelationshipStatus relationshipStatus
    ) {
        return relationshipRepository.save(
                Relationship.builder()
                        .fromUser(from)
                        .toUser(to)
                        .relationshipType(relationshipType)
                        .relationshipStatus(relationshipStatus)
                        .build()
        );
    }

    private CustomNameMapping saveCustomName(User from, User to) {
        return customNameMappingRepository.save(
                CustomNameMapping.builder()
                        .fromUser(from)
                        .toUser(to)
                        .customName(to.getName())
                        .build()
        );
    }

    private void verifyExistsRelationship(User fromUser, User toUser, RelationshipType relationshipType) {
        if (relationshipRepository.existsSameTypeBetweenUsers(
                fromUser.getId(), toUser.getId(), relationshipType
        )) {
            throw new RelationshipException(ErrorCode.ALREADY_RELATIONSHIP_EXISTS);
        }
    }
}
