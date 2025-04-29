package com.littlebank.finance.domain.user.dto.response;

import com.littlebank.finance.domain.relationship.domain.Relationship;
import com.littlebank.finance.domain.relationship.domain.RelationshipStatus;
import com.littlebank.finance.domain.relationship.domain.RelationshipType;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@Builder
@AllArgsConstructor
public class UserSearchResponse {
    private Long searchUserId;
    private String email;
    private String name;
    private String profileImagePath;
    private UserRole role;
    private List<RelationResponse> relationships;

    public static UserSearchResponse of(User searchUser, List<Relationship> relationships) {
        return UserSearchResponse.builder()
                .searchUserId(searchUser.getId())
                .email(searchUser.getEmail())
                .name(searchUser.getName())
                .profileImagePath(searchUser.getProfileImagePath())
                .role(searchUser.getRole())
                .relationships(relationships.stream()
                        .map(e -> RelationResponse.of(e))
                        .collect(Collectors.toList())
                )
                .build();
    }

    @Getter
    @Builder
    @AllArgsConstructor
    private static class RelationResponse {
        private String customName;
        private RelationshipType relationshipType;
        private RelationshipStatus relationshipStatus;

        private static RelationResponse of(Relationship relationship) {
            return RelationResponse.builder()
                    .customName(relationship.getCustomName())
                    .relationshipType(relationship.getRelationshipType())
                    .relationshipStatus(relationship.getRelationshipStatus())
                    .build();
        }

    }
}
