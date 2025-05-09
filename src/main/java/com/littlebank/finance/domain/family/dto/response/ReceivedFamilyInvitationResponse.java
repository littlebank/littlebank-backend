package com.littlebank.finance.domain.family.dto.response;

import com.littlebank.finance.domain.family.domain.FamilyMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ReceivedFamilyInvitationResponse {
    private Long familyMemberId;
    private Long familyId;
    private Long inviterId;
    private String inviterName;
    private LocalDateTime invitedDate;

    public static ReceivedFamilyInvitationResponse of(FamilyMember member) {
        return ReceivedFamilyInvitationResponse.builder()
                .familyMemberId(member.getId())
                .familyId(member.getFamily().getId())
                .inviterId(member.getInvitedBy().getId())
                .inviterName(member.getInvitedBy().getName())
                .invitedDate(member.getCreatedDate())
                .build();
    }
}
