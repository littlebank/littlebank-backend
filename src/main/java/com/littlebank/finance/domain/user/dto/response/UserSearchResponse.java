package com.littlebank.finance.domain.user.dto.response;

import com.littlebank.finance.domain.relationship.domain.RelationshipStatus;
import com.littlebank.finance.domain.relationship.domain.RelationshipType;
import com.littlebank.finance.domain.user.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


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


    @Getter
    @Builder
    @AllArgsConstructor
    public static class RelationResponse {
        private String customName;
        private RelationshipType relationshipType;
        private RelationshipStatus relationshipStatus;

    }
}
