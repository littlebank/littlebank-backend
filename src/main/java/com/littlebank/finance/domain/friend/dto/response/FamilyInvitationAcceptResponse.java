package com.littlebank.finance.domain.friend.dto.response;

import com.littlebank.finance.domain.family.domain.FamilyMember;
import com.littlebank.finance.domain.family.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FamilyInvitationAcceptResponse {
    private Long familyMemberId;
    private Status status;

    public static FamilyInvitationAcceptResponse of(FamilyMember member) {
        return FamilyInvitationAcceptResponse.builder()
                .familyMemberId(member.getId())
                .status(member.getStatus())
                .build();
    }
}
