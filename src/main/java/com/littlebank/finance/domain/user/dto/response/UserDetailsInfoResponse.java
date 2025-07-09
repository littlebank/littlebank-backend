package com.littlebank.finance.domain.user.dto.response;

import com.littlebank.finance.domain.friend.dto.response.CommonFriendInfoResponse;
import com.littlebank.finance.domain.user.domain.SchoolType;
import com.littlebank.finance.domain.user.domain.constant.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDetailsInfoResponse {
    private Long userId;
    private String email;
    private String realName;
    private String statusMessage;
    private String phone;
    private String rrn;
    private String bankName;
    private String bankCode;
    private String bankAccount;
    private String profileImagePath;
    private String schoolName;
    private SchoolType schoolType;
    private Integer region;
    private String address;
    private UserRole role;
    private CommonFriendInfoResponse friendInfo;
    private Long friendCount;
    private Long missionCount;
    private Long challengeCount;

}
