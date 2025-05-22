package com.littlebank.finance.domain.point.domain;

import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Getter
@SQLDelete(sql = "UPDATE payment SET is_deleted = true WHERE family_id = ?")
@Where(clause = "is_deleted = false")
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
    private String payMethod;  // 카드, kakaopay 등
    @Column(length = 50)
    private String pgProvider;  // tosspay, kakaopay 등
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PaymentStatus status;
    private LocalDateTime paidAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false)
    private Boolean isDeleted;

    @Builder
    public Payment(String impUid, String merchantUid, Integer amount, String payMethod, String pgProvider, PaymentStatus status, LocalDateTime paidAt, User user, Boolean isDeleted) {
        this.impUid = impUid;
        this.merchantUid = merchantUid;
        this.amount = amount;
        this.payMethod = payMethod;
        this.pgProvider = pgProvider;
        this.status = status;
        this.paidAt = paidAt;
        this.user = user;
        this.isDeleted = isDeleted;
    }
}
