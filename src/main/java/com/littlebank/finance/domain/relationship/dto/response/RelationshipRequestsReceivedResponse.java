package com.littlebank.finance.domain.relationship.dto.response;

import com.littlebank.finance.domain.relationship.domain.Relationship;
import com.littlebank.finance.domain.relationship.domain.RelationshipStatus;
import com.littlebank.finance.domain.relationship.domain.RelationshipType;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class RelationshipRequestsReceivedResponse {
    private UserInfo user;
    private RelationInfo relation;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class UserInfo {
        private Long userId;
        private String userName;
        private String profileImagePath;
        private UserRole role;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class RelationInfo {
        private Long relationshipId;
        private RelationshipType relationshipType;
        private RelationshipStatus relationshipStatus;
        private LocalDateTime requestedAt;
    }

    public static RelationshipRequestsReceivedResponse of(User user, Relationship relationship) {
        return RelationshipRequestsReceivedResponse.builder()
                .user(UserInfo.builder()
                        .userId(user.getId())
                        .userName(user.getName())
                        .profileImagePath(user.getProfileImagePath())
                        .role(user.getRole())
                        .build())
                .relation(RelationInfo.builder()
                        .relationshipId(relationship.getId())
                        .relationshipType(relationship.getRelationshipType())
                        .relationshipStatus(relationship.getRelationshipStatus())
                        .requestedAt(relationship.getCreatedDate())
                        .build())
                .build();
    }
}
