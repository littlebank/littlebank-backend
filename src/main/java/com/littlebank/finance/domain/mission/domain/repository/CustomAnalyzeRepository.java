package com.littlebank.finance.domain.mission.domain.repository;

import com.littlebank.finance.domain.mission.dto.response.AnalyzeReportResponse;

import java.util.Optional;

public interface CustomAnalyzeRepository {
    Optional<AnalyzeReportResponse> getAnalyzeReportResponse(Long memberId, Integer period);
}
