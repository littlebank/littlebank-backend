package com.littlebank.finance.domain.chat.domain;

import com.littlebank.finance.domain.chat.domain.constant.EventType;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomEventLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_log_id")
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType eventType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom room;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    private User agent;
    @OneToMany(mappedBy = "log", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoomEventLogDetail> eventLogDetails = new ArrayList<>();

    @Builder
    public ChatRoomEventLog(EventType eventType, ChatRoom room, User agent) {
        this.eventType = eventType;
        this.room = room;
        this.agent = agent;
    }
}
