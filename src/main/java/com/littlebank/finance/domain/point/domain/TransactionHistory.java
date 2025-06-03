package com.littlebank.finance.domain.point.domain;

import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransactionHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_history_id")
    private Long id;
    @Column(nullable = false)
    private Integer pointAmount;
    @Column(length = 100, nullable = false)
    private String message;
    @Column(nullable = false)
    private Integer senderRemainingPoint;
    @Column(nullable = false)
    private Integer receiverRemainingPoint;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Builder
    public TransactionHistory(Integer pointAmount, String message, Integer senderRemainingPoint, Integer receiverRemainingPoint, User sender, User receiver) {
        this.pointAmount = pointAmount;
        this.message = message;
        this.senderRemainingPoint = senderRemainingPoint;
        this.receiverRemainingPoint = receiverRemainingPoint;
        this.sender = sender;
        this.receiver = receiver;
    }
}
