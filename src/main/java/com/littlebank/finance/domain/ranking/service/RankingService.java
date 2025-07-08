package com.littlebank.finance.domain.ranking.service;

import com.littlebank.finance.domain.ranking.domain.repository.RankingRepository;
import com.littlebank.finance.domain.ranking.dto.request.TargetRequestDto;
import com.littlebank.finance.domain.ranking.dto.response.GoalRankingResponseDto;
import com.littlebank.finance.domain.ranking.dto.response.TargetResponseDto;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.error.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;


@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class RankingService {
    private final UserRepository userRepository;
    private final RankingRepository rankingRepository;
    public TargetResponseDto setTargetAmount(Long userId, TargetRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        Integer targetAmount = request.getTargetAmount();
        user.setTargetAmount(targetAmount);
        return TargetResponseDto.of(user,targetAmount);
    }

    public GoalRankingResponseDto getGoalRankingByTargetAmount(Long userId, Long targetId, YearMonth month) {
        GoalRankingResponseDto response = rankingRepository.findMonthlyTargetUserStat(userId, targetId, month);
        return response;
    }
}
