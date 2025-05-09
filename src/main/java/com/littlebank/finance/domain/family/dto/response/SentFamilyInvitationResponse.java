package com.littlebank.finance.domain.family.dto.response;

import com.littlebank.finance.domain.family.domain.FamilyMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class SentFamilyInvitationResponse {
    private Long familyMemberId;
    private Long inviteeId;
    private String inviteeName;
    private String inviterName;
    private LocalDateTime invitedDate;

    public static SentFamilyInvitationResponse of(FamilyMember member) {
        return SentFamilyInvitationResponse.builder()
                .familyMemberId(member.getId())
                .inviteeId(member.getUser().getId())
                .inviteeName(member.getUser().getName())
                .inviterName(member.getInvitedBy().getName())
                .invitedDate(member.getCreatedDate())
                .build();
    }
}
