package com.littlebank.finance.domain.survey.domain;

import com.littlebank.finance.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="quiz")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Survey extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private Long id;
    @Column(nullable = false, length = 100)
    private String question;
    @Column(nullable = false, length = 100)
    private String optionA;
    @Column(nullable = false, length = 100)
    private String optionB;
    @Column(nullable = false, length = 100)
    private String optionC;
    @Column(nullable = false)
    private Integer voteA = 0;
    @Column(nullable = false)
    private Integer voteB = 0;
    @Column(nullable = false)
    private Integer voteC = 0;
    @Column(nullable = false)
    private Boolean isDeleted;

    @Builder
    public Survey(String question, String optionA, String optionB, String optionC,
                  Integer voteA, Integer voteB, Integer voteC, Boolean isDeleted) {
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.voteA = voteA != null ? voteA : 0 ;
        this.voteB = voteB != null ? voteB : 0 ;
        this.voteC = voteC != null ? voteC : 0 ;
        this.isDeleted = isDeleted == null ? false : isDeleted;
    }

    public void update(String question, String optionA, String optionB, String optionC) {
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
    }

    public void setVoteA(int voteA) {
        this.voteA = voteA;
    }
    public void setVoteB(int voteB) {
        this.voteB = voteB;
    }
    public void setVoteC(int voteC) {
        this.voteC = voteC;
    }
}
