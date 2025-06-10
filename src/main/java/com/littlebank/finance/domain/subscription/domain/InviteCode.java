package com.littlebank.finance.domain.subscription.domain;

import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Table(name = "invitecode")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InviteCode extends BaseEntity {
    @Id @GeneratedValue
    private Long id;
    @Column(nullable = false, unique = true)
    private String code;
    @Column(nullable = false)
    private boolean used;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;
    @OneToOne
    private User redeemedBy;

    @Builder
    public InviteCode(String code, boolean used, Subscription subscription) {
        this.code = code;
        this.used = used;
        this.subscription = subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }
}
