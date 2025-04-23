package com.littlebank.finance.domain.chat.domain;

import com.littlebank.finance.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name="chat_room")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseEntity {
    @Id
    @Column(length=100)
    private String id;

    @Column(name="room_name",nullable = false,length = 50)
    private String name;

    @Builder
    public ChatRoom(String id, String name) {
        this.id = id;
        this.name = name;
    }
}