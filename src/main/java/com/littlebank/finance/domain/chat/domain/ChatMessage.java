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
    private String content;  // 텍스트 내용 or 이미지 URL
    @Column(nullable = false)
    private LocalDateTime timestamp; // 메시지가 생성된 시점
    @Column(nullable = false)
    private Boolean isDeleted;

    @Builder
    public ChatMessage(MessageType messageType, ChatRoom room, User sender, String content, LocalDateTime timestamp, Boolean isDeleted) {
        this.messageType = messageType;
        this.room = room;
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
        this.isDeleted = isDeleted == null ? false : isDeleted;
    }
}
