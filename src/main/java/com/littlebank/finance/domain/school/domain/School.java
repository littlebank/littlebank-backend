package com.littlebank.finance.domain.school.domain;

import com.littlebank.finance.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "school")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class School extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100)
    private String name; // 학교명
    @Column(length = 50)
    private String gubun; // 학교종류 (초/중/고)
    @Column(length = 50)
    private String type; // 학교유형
    @Column(length = 50)
    private String estType; // 설립유형
    @Column(length = 50)
    private Integer region; // 지역
    @Column(length = 200)
    private String address; // 주소
    private String detailUrl; // 상세 링크

    public static School of(String name, String gubun, String type, String estType, Integer region, String address, String detailUrl) {
        School school = new School();
        school.name = name;
        school.gubun = gubun;
        school.type = type;
        school.estType = estType;
        school.region = region;
        school.address = address;
        school.detailUrl = detailUrl;
        return school;
    }
}
