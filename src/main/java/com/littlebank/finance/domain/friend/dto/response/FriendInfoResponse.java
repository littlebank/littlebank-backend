package com.littlebank.finance.domain.friend.dto.response;

import com.littlebank.finance.domain.user.dto.response.CommonUserInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FriendInfoResponse {
    private CommonUserInfoResponse userInfo;
    private CommonFriendInfoResponse friendInfo;
}
