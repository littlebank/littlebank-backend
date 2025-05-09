package com.littlebank.finance.domain.report.dto.request;


import com.littlebank.finance.domain.report.domain.ReportType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportRequestDto {
    private ReportType type;
    private Long targetId;
}
