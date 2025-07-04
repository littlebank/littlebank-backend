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
    private Long redeemedById;
    private String redeemedByName;

    public static InviteCodeResponseDto from(InviteCode inviteCode) {
        return InviteCodeResponseDto.builder()
                .code(inviteCode.getCode())
                .used(inviteCode.isUsed())
                .redeemedById(inviteCode.getRedeemedBy() != null ? inviteCode.getRedeemedBy().getId() : null)
                .redeemedByName(inviteCode.getRedeemedBy() != null ? inviteCode.getRedeemedBy().getName() : null)
                .build();
    }
}
