package com.littlebank.finance.global.common;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CustomPageResponse<T> {
    private List<T> data = new ArrayList<>();
    private int totalPage;
    private long totalElement;
    private long pageNumber;

    @Builder
    public CustomPageResponse(List<T> data, int totalPage, long totalElement, long pageNumber) {
        this.data = data;
        this.totalPage = totalPage;
        this.totalElement = totalElement;
        this.pageNumber = pageNumber;
    }

    public static <T> CustomPageResponse<T> of(Page<T> page) {
        return CustomPageResponse.<T>builder()
                .data(page.getContent())
                .totalPage(page.getTotalPages())
                .totalElement(page.getTotalElements())
                .pageNumber(page.getNumber())
                .build();
    }

    public static <T> CustomPageResponse<T> of(Page<T> page, List<T> data) {
        return CustomPageResponse.<T>builder()
                .data(data)
                .totalPage(page.getTotalPages())
                .totalElement(page.getTotalElements())
                .pageNumber(page.getNumber())
                .build();
    }
}
