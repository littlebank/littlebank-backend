package com.littlebank.finance.domain.chat.domain;

import com.littlebank.finance.domain.chat.domain.constant.RoomRange;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@SQLDelete(sql = "UPDATE chat_room SET is_deleted = true WHERE chat_room_id = ?")
@Where(clause = "is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;
    @Column(nullable = false, length = 50)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "room_range", nullable = false)
    private RoomRange range;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;
    @Column(nullable = false)
    private Long lastMessageId;
    @Column(nullable = false)
    private Boolean isDeleted;

    @Builder
    public ChatRoom(String name, RoomRange range, User createdBy, Long lastMessageId, Boolean isDeleted) {
        this.name = name;
        this.range = range;
        this.createdBy = createdBy;
        this.lastMessageId = lastMessageId == null ? 0L : lastMessageId;
        this.isDeleted = isDeleted == null ? false : isDeleted;
    }

    public void updateLastMessageId(ChatMessage message) {
        if (this.lastMessageId < message.getId()) {
            this.lastMessageId = message.getId();
        }
    }

}