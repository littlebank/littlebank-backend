package com.littlebank.finance.domain.point.domain;

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
public class Refund extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refund_id")
    private Long id;
    @Column(nullable = false)
    private Integer requestedAmount; // 요청환전금액
    @Column(nullable = false)
    private Integer processedAmount; // 실제환전금액
    @Column(nullable = false)
    private Integer remainingPoint;
    @Enumerated(EnumType.STRING)
    private RefundStatus status;
    private LocalDateTime exchangedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Refund(Integer requestedAmount, Integer processedAmount, Integer remainingPoint, RefundStatus status, LocalDateTime exchangedAt, User user) {
        this.requestedAmount = requestedAmount;
        this.processedAmount = processedAmount;
        this.remainingPoint = remainingPoint;
        this.status = status;
        this.exchangedAt = exchangedAt;
        this.user = user;
    }
}
