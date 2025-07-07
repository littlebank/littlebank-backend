package com.littlebank.finance.domain.family.dto.response;

import com.littlebank.finance.domain.user.domain.constant.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FamilyMemberInfoResponse {
    private Long familyMemberId;
    private String nickname;
    private Long userId;
    private String email;
    private String realName;
    private String phone;
    private String rrn;
    private String bankName;
    private String bankAccount;
    private String profileImagePath;
    private UserRole role;
}
