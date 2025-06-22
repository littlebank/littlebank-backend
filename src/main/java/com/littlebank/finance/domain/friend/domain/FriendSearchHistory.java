package com.littlebank.finance.domain.friend.domain;

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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "friend_id가"}))
public class FriendSearchHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_search_history_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id", nullable = false)
    private Friend friend;
    @Column(nullable = false)
    private LocalDateTime searchAt;

    @Builder
    public FriendSearchHistory(User user, Friend friend, LocalDateTime searchAt) {
        this.user = user;
        this.friend = friend;
        this.searchAt = searchAt == null ? LocalDateTime.now() : searchAt;
    }

    public void searchAgain() {
        this.searchAt = LocalDateTime.now();
    }

}
