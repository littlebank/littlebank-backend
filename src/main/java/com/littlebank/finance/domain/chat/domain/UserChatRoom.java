package com.littlebank.finance.domain.chat.domain;

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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"room", "user"})
)
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
    @Column(nullable = false)
    private Boolean isJoined;
    @Column(nullable = false)
    private LocalDateTime joinedDate;
    @Version
    private Long version;

    @Builder
    public UserChatRoom(ChatRoom room, User user, LocalDateTime displayIdx, Long lastReadMessageId, Boolean isJoined, LocalDateTime joinedDate) {
        this.room = room;
        this.user = user;
        this.displayIdx = displayIdx == null ? LocalDateTime.now() : displayIdx;
        this.lastReadMessageId = lastReadMessageId == null ? 0L : lastReadMessageId;
        this.isJoined = isJoined == null ? true : isJoined;
        this.joinedDate = joinedDate == null ? LocalDateTime.now() : joinedDate;
    }

    public void updateLastReadMessageId(Long messageId) {
        if (this.lastReadMessageId < messageId) {
            this.lastReadMessageId = messageId;
        }
    }

    public void joinPrivateRoom() {
        this.isJoined = true;
        this.joinedDate = LocalDateTime.now();
    }

    public void leavePrivateRoom() {
        this.isJoined = false;
    }

}
