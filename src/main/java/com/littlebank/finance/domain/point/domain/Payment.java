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
@Table(name = "payment", uniqueConstraints = {
        @UniqueConstraint(columnNames = "impUid")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;
    @Column(nullable = false, length = 100)
    private String impUid;  // 포트원 결제 고유 ID
    @Column(length = 100)
    private String merchantUid;  // 주문번호
    @Column(nullable = false)
    private Integer amount;  // 결제 금액
    @Column(length = 50)
    private String payMethod;  // 결제 방법(카드, 포인트 등)
    @Column(length = 50)
    private String pgProvider;  // 이용한 결제대행사(kakaopay, tosspay 등)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private Integer remainingPoint;  // 결제 시점 남은 포인트
    private LocalDateTime paidAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Payment(String impUid, String merchantUid, Integer amount, String payMethod, String pgProvider,
                   PaymentStatus status, Integer remainingPoint, LocalDateTime paidAt, User user) {
        this.impUid = impUid;
        this.merchantUid = merchantUid;
        this.amount = amount;
        this.payMethod = payMethod;
        this.pgProvider = pgProvider;
        this.status = status;
        this.remainingPoint = remainingPoint;
        this.paidAt = paidAt;
        this.user = user;
    }

    public void recordRemainingPoint(User user) {
        this.remainingPoint = user.getPoint();
    }
}
