package com.littlebank.finance.domain.user.dto.response;

import com.littlebank.finance.domain.friend.dto.response.CommonFriendInfoResponse;
import com.littlebank.finance.domain.user.domain.constant.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSearchResponse {
    private Long searchUserId;
    private String email;
    private String name;
    private String rrn;
    private String statusMessage;
    private String profileImagePath;
    private UserRole role;
    private CommonFriendInfoResponse friendInfo;
}
