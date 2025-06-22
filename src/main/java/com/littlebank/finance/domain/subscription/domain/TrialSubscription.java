package com.littlebank.finance.domain.subscription.domain;

import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "trial_subscription")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TrialSubscription extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private boolean used; // littlebank 코드 사용 여부

    @Builder
    public TrialSubscription(User user, LocalDateTime startDate, LocalDateTime endDate, boolean used) {
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.used = used;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(endDate);
    }
}
