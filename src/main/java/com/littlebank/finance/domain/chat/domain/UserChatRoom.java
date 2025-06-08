package com.littlebank.finance.domain.chat.domain;

import com.littlebank.finance.domain.user.domain.User;
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
@SQLDelete(sql = "UPDATE user_chat_room SET is_deleted = true WHERE user_chat_room_id = ?")
@Where(clause = "is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserChatRoom {
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
    private Boolean isDeleted;

    @Builder
    public UserChatRoom(ChatRoom room, User user, LocalDateTime displayIdx, Boolean isDeleted) {
        this.room = room;
        this.user = user;
        this.displayIdx = displayIdx == null ? LocalDateTime.now() : displayIdx;
        this.isDeleted = isDeleted == null ? false : isDeleted;
    }
}
