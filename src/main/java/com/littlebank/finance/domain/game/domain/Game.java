package com.littlebank.finance.domain.game.domain;

import com.littlebank.finance.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.N;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "game")
@SQLDelete(sql = "UPDATE game SET is_deleted = true WHERE game_id = ?")
@Where(clause = "is_deleted = false")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Game extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private Long id;
    @Column(nullable = false, length = 100)
    private String question;
    @Column(nullable = false, length = 100)
    private String option_a;
    @Column(nullable = false, length = 100)
    private String option_b;
    @Column(nullable = false, length = 100)
    private Integer vote_a;
    @Column(nullable = false, length = 100)
    private Integer vote_b;
    @Column(nullable = false)
    private Boolean isDeleted;

    @Builder
    public Game(String question, String option_a, String option_b, Integer vote_a, Integer vote_b, Boolean isDeleted) {
        this.question = question;
        this.option_a = option_a;
        this.option_b = option_b;
        this.vote_a = vote_a;
        this.vote_b = vote_b;
        this.isDeleted = isDeleted == null ? false : isDeleted;
    }
}
