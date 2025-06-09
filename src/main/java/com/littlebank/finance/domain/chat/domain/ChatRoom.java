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
    @Column(name = "room_type", nullable = false)
    private RoomType type;
    @Enumerated(EnumType.STRING)
    @Column(name = "room_range", nullable = false)
    private RoomRange range;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;
    @Column(nullable = false)
    private Boolean isDeleted;

    @Builder
    public ChatRoom(String name, RoomType type, RoomRange range, User createdBy, Boolean isDeleted) {
        this.name = name;
        this.type = type;
        this.range = range;
        this.createdBy = createdBy;
        this.isDeleted = isDeleted == null ? false : isDeleted;
    }
}