package com.littlebank.finance.domain.user.domain;

import com.littlebank.finance.domain.subscription.domain.Subscription;
import com.littlebank.finance.domain.user.domain.constant.Authority;
import com.littlebank.finance.domain.user.domain.constant.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_withdraw")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserWithdraw {
    @Id
    private Long userId;
    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(length = 50)
    private String email;
    @Column(length = 20)
    private String name;
    @Column(length = 100)
    private String statusMessage;
    @Column(length = 11)
    private String phone;
    @Column(length = 6)
    private String rrn;
    @Column(length = 10)
    private String bankName;
    @Column(length = 3)
    private String bankCode;
    @Column(length = 20)
    private String bankAccount;
    @Column(length = 6)
    private String accountPin;
    @Column(length = 150)
    private String profileImagePath;
    private Integer point;
    private Integer accumulatedPoint;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Enumerated(EnumType.STRING)
    private Authority authority;
    private Boolean isSubscribe;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;
    @Column(nullable = false)
    private String reason;

    @Builder
    public UserWithdraw(
            User user, String email, String name, String statusMessage, String phone, String rrn,
            String bankName, String bankCode, String bankAccount, String accountPin, String profileImagePath,
            Integer point, Integer accumulatedPoint, UserRole role, Authority authority, Boolean isSubscribe,
            Subscription subscription, String reason
    ) {
        this.user = user;
        this.email = email;
        this.name = name;
        this.statusMessage = statusMessage;
        this.phone = phone;
        this.rrn = rrn;
        this.bankName = bankName;
        this.bankCode = bankCode;
        this.bankAccount = bankAccount;
        this.accountPin = accountPin;
        this.profileImagePath = profileImagePath;
        this.point = point;
        this.accumulatedPoint = accumulatedPoint;
        this.role = role;
        this.authority = authority;
        this.isSubscribe = isSubscribe;
        this.subscription = subscription;
        this.reason = reason;
    }

    public static UserWithdraw of(User user, String reason) {
        return UserWithdraw.builder()
                .user(user)
                .email(user.getEmail())
                .name(user.getName())
                .statusMessage(user.getStatusMessage())
                .phone(user.getPhone())
                .rrn(user.getRrn())
                .bankName(user.getBankName())
                .bankCode(user.getBankCode())
                .bankAccount(user.getBankAccount())
                .accountPin(user.getAccountPin())
                .profileImagePath(user.getProfileImagePath())
                .point(user.getPoint())
                .accumulatedPoint(user.getAccumulatedPoint())
                .role(user.getRole())
                .authority(user.getAuthority())
                .isSubscribe(user.getIsSubscribe())
                .subscription(user.getSubscription())
                .reason(reason)
                .build();
    }
}
