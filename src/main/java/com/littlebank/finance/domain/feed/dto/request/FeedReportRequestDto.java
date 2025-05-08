package com.littlebank.finance.domain.feed.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class FeedReportRequestDto {
    @NotBlank
    @Size(max = 1000)
    private String reason;
}
