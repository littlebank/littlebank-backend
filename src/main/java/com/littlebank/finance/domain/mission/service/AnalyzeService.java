package com.littlebank.finance.domain.mission.service;

import com.littlebank.finance.domain.mission.domain.repository.MissionRepository;
import com.littlebank.finance.domain.mission.dto.response.AnalyzeReportResponse;
import com.littlebank.finance.domain.mission.exception.MissionException;
import com.littlebank.finance.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AnalyzeService {
    private final MissionRepository missionRepository;

    @Transactional(readOnly = true)
    public AnalyzeReportResponse getAnalyzeReport(Long memberId, Integer period) {
        return missionRepository.getAnalyzeReportResponse(memberId, period)
                .orElseThrow(() -> new MissionException(ErrorCode.MISSION_NOT_FOUND));
    }

}
