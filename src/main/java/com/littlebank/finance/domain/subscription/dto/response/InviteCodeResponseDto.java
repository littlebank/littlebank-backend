package com.littlebank.finance.domain.subscription.dto.response;

import com.littlebank.finance.domain.subscription.domain.InviteCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class InviteCodeResponseDto {
    private String code;
    private boolean used;
    private String redeemedByEmail;

//    public static InviteCodeResponseDto of(InviteCode inviteCode) {
//
//    }
}
