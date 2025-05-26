package com.littlebank.finance.domain.game.domain;

import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "game_vote")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameVote extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_vote_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 1)
    private GameChoice choice;

    @Builder
    public GameVote(Game game, User user, GameChoice choice) {
        this.game = game;
        this.user = user;
        this.choice = choice;
    }
}
