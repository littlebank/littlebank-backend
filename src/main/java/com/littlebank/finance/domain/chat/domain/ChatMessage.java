package com.littlebank.finance.domain.chat.domain;

import com.littlebank.finance.domain.chat.domain.constant.MessageType;
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
@SQLDelete(sql = "UPDATE chat_message SET is_deleted = true WHERE chat_message_id = ?")
@Where(clause = "is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType messageType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room", nullable = false)
    private ChatRoom room;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender", nullable = false)
    private User sender;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private LocalDateTime timestamp;
    @Column(nullable = false)
    private Boolean isDeleted;
    @Column(nullable = false)
    private Integer readCount;
    @Version
    private Long version;

    @Builder
    public ChatMessage(MessageType messageType, ChatRoom room, User sender, String content, LocalDateTime timestamp, Boolean isDeleted, int readCount) {
        this.messageType = messageType;
        this.room = room;
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
        this.isDeleted = isDeleted == null ? false : isDeleted;
        this.readCount = readCount;
    }

    public void readMessage() {
        this.readCount -= 1;
    }
}
