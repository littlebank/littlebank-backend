package com.littlebank.finance.domain.feed.dto.response;

import com.littlebank.finance.domain.feed.dto.request.FeedReportRequestDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeedReportResponseDto {
    private Long reportId;
    private Long feedId;
    private Long reporterId;
    private String reason;

    public static FeedReportResponseDto of(Long reportId, Long feedId, Long reporterId, String reason) {
        return FeedReportResponseDto.builder()
                .reportId(reportId)
                .feedId(feedId)
                .reporterId(reporterId)
                .reason(reason)
                .build();
    }
}
