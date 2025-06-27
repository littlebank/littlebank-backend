package com.littlebank.finance.domain.report.dto.response;


import com.littlebank.finance.domain.report.domain.ReportType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportResponseDto {
    private ReportType type;
    private Long targetId;
    private Long reporterId;
    private String reporterName;
    private Long targetUserId;
    private String targetUserName;
    private String targetContent;

    public static ReportResponseDto of (ReportType type, Long targetId, Long reporterId, String reporterName,
                                        Long targetUserId, String targetUserName, String targetContent) {
        return ReportResponseDto.builder()
                .type(type)
                .reporterId(reporterId)
                .reporterName(reporterName)
                .targetId(targetId)
                .targetUserId(targetUserId)
                .targetUserName(targetUserName)
                .targetContent(targetContent)
                .build();
    }
}
