package com.littlebank.finance.domain.ranking.domain.repository;

import com.littlebank.finance.domain.ranking.dto.response.GoalRankingResponseDto;
import java.time.YearMonth;

public interface CustomRankingRepository {
    GoalRankingResponseDto findMonthlyTargetUserStat(Long userId, YearMonth month);
}
