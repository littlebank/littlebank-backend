package com.littlebank.finance.domain.user.dto.response;

import com.littlebank.finance.domain.relationship.domain.RelationshipStatus;
import com.littlebank.finance.domain.relationship.domain.RelationshipType;
import com.littlebank.finance.domain.user.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class UserDetailsInfoResponse {
    private Long userId;
    private String email;
    private String realName;
    private String customName;
    private String phone;
    private String rrn;
    private String bankName;
    private String bankCode;
    private String bankAccount;
    private String profileImagePath;
    private UserRole role;
    private List<RelationInfo> relation;

    @Getter
    @AllArgsConstructor
    public static class RelationInfo {
        private Long relationshipId;
        private RelationshipType relationshipType;
        private RelationshipStatus relationshipStatus;
        private LocalDateTime requestedAt;
    }

}
