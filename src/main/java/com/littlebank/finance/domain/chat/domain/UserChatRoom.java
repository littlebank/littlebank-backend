package com.littlebank.finance.domain.chat.domain;

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
public class UserChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_chat_room_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room", nullable = false)
    private ChatRoom room;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user", nullable = false)
    private User user;
    @Column(nullable = false)
    private LocalDateTime displayIdx;
    @Column(nullable = false)
    private Long lastReadMessageId;
    @Version
    private Long version;

    @Builder
    public UserChatRoom(ChatRoom room, User user, LocalDateTime displayIdx, Long lastReadMessageId) {
        this.room = room;
        this.user = user;
        this.displayIdx = displayIdx == null ? LocalDateTime.now() : displayIdx;
        this.lastReadMessageId = lastReadMessageId == null ? 0L : lastReadMessageId;
    }

    public void updateLastReadMessageId(Long messageId) {
        if (this.lastReadMessageId < messageId) {
            this.lastReadMessageId = messageId;
        }
    }

}
