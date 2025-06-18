package com.littlebank.finance.domain.subscription.dto.response;

public record CommonResponse<T> (boolean success, T data) {
    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(true, data);
    }
    public static <T> CommonResponse<T> fail(T data) {
        return new CommonResponse<>(false, data);
    }
}
