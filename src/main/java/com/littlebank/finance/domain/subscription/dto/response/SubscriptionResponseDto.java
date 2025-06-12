package com.littlebank.finance.domain.subscription.dto.response;

import com.littlebank.finance.domain.subscription.domain.InviteCode;
import com.littlebank.finance.domain.subscription.domain.Subscription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class SubscriptionResponseDto {
    private Long subscriptionId;
    private Long ownerId;
    private Integer seat;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<Long> members;
    private Map<String, Long> inviteCodes;
    private Integer left;
    public static SubscriptionResponseDto of(Subscription subscription) {
        Map<String, Long> inviteCodes = new HashMap<>();
        for (InviteCode invite : subscription.getInviteCodes()) {
            inviteCodes.put(invite.getCode(),
                    invite.getRedeemedBy() != null ? invite.getRedeemedBy().getId() : null);
        }

        return SubscriptionResponseDto.builder()
                .subscriptionId(subscription.getId())
                .ownerId(subscription.getOwner().getId())
                .seat(subscription.getSeat())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .members(subscription.getMembers().stream()
                        .map(user -> user.getId())
                                .toList())
                .inviteCodes(inviteCodes)
                .left(subscription.getSeat() -1 -(int) subscription.getInviteCodes().stream()
                        .filter(InviteCode::isUsed)
                        .count())
                .build();
    }
}
