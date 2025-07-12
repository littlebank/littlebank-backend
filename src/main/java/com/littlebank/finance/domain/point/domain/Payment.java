package com.littlebank.finance.domain.point.domain;

import com.littlebank.finance.domain.point.domain.constant.TossPaymentMethod;
import com.littlebank.finance.domain.point.domain.constant.TossPaymentStatus;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;
    @Column(nullable = false, unique = true)
    private String tossPaymentKey;
    @Column(nullable = false)
    private String tossOrderId;
    @Column(nullable = false)
    private Integer amount;
    @Column(nullable = false)
    private Integer remainingPoint;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private TossPaymentMethod tossPaymentMethod;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private TossPaymentStatus tossPaymentStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private LocalDateTime paidAt;

    @Builder
    public Payment(
            String tossPaymentKey, String tossOrderId, Integer amount, Integer remainingPoint, TossPaymentMethod tossPaymentMethod,
            TossPaymentStatus tossPaymentStatus, User user, LocalDateTime paidAt
    ) {
        this.tossPaymentKey = tossPaymentKey;
        this.tossOrderId = tossOrderId;
        this.amount = amount;
        this.tossPaymentMethod = tossPaymentMethod;
        this.tossPaymentStatus = tossPaymentStatus;
        this.user = user;
        this.paidAt = paidAt;
    }

}
