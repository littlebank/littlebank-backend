package com.littlebank.finance.domain.subscription.dto.response;

import com.littlebank.finance.domain.subscription.domain.InviteCode;
import com.littlebank.finance.domain.subscription.domain.Subscription;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.dto.response.UserEmailDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class SubscriptionResponseDto {
    private Long subscriptionId;
    private Long ownerId;
    private Integer seat;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<UserEmailDto> members;
    private List<InviteCode> inviteCodes;

    public static SubscriptionResponseDto of(Subscription subscription) {
        return SubscriptionResponseDto.builder()
                .subscriptionId(subscription.getId())
                .ownerId(subscription.getOwner().getId())
                .seat(subscription.getSeat())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .members(subscription.getMembers().stream()
                        .map(user -> new UserEmailDto(user.getId(), user.getEmail()))
                        .toList())
                .inviteCodes(subscription.getInviteCodes())
                .build();
    }
}
