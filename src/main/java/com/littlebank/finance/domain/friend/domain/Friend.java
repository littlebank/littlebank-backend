package com.littlebank.finance.domain.friend.domain;

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
@Table(name = "friend", uniqueConstraints = @UniqueConstraint(columnNames = {"from_user", "to_user"}))
public class Friend extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user", nullable = false)
    private User fromUser;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user", nullable = false)
    private User toUser;
    @Column(length = 50, nullable = false)
    private String customName;
    @Column(nullable = false)
    private Boolean isBlocked;
    @Column(nullable = false)
    private Boolean isBestFriend;

    @Builder
    public Friend(User fromUser, User toUser, String customName, Boolean isBlocked, Boolean isBestFriend) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.customName = customName;
        this.isBlocked = isBlocked == null ? false : isBlocked;
        this.isBestFriend = isBestFriend == null ? false : isBestFriend;
    }

    public void markBestFriend() {
        this.isBestFriend = true;
    }

    public void blocking() {
        this.isBlocked = true;
    }

    public void unblocking() {
        this.isBlocked = false;
    }

    public void rename(String chaneName) {
        this.customName = chaneName;
    }
}
