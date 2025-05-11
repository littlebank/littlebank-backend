package com.littlebank.finance.domain.notification.domain;

import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 10)
    private NotificationType type;

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    @Builder
    public static Notification of (User receiver, NotificationType type, Long targetId, String message, boolean isRead) {
        Notification n = new Notification();
        n.receiver = receiver;
        n.type = type;
        n.targetId = targetId;
        n.message = message;
        n.isRead = isRead;
        return n;
    }

    public void markAsRead() {
        this.isRead = true;
    }
}
