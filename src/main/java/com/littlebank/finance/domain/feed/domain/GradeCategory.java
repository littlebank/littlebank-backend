package com.littlebank.finance.domain.feed.domain;

public enum GradeCategory {
    ELEMENTARY("초등학생"),
    MIDDLE("중학생"),
    HIGH("고등학생"),
    ALL("전체");

    private final String description;

    GradeCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
