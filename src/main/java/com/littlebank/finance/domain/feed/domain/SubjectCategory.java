package com.littlebank.finance.domain.feed.domain;

public enum SubjectCategory {
    KOREAN("국어"),
    MATH("수학"),
    ENGLISH("영어"),
    SOCIETY("사회"),
    SCIENCE("과학"),
    ALL("전체");

    private final String description;

    SubjectCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
