package com.littlebank.finance.domain.survey.service;

import com.littlebank.finance.domain.survey.domain.Survey;
import com.littlebank.finance.domain.survey.domain.repository.SurveyRepository;
import com.littlebank.finance.domain.survey.dto.request.SurveyVoteRequestDto;
import com.littlebank.finance.domain.survey.dto.response.SurveyVoteResponseDto;
import com.littlebank.finance.domain.survey.exception.SurveyException;
import com.littlebank.finance.global.error.exception.ErrorCode;
import com.littlebank.finance.global.redis.RedisDao;
import com.littlebank.finance.global.redis.RedisPolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final RedisDao redisDao;
    private final RedissonClient redissonClient;

    public SurveyVoteResponseDto joinQuiz(Long userId, Long quizId, SurveyVoteRequestDto request) {
        String redisKey = RedisPolicy.QUIZ_VOTE_PREFIX + quizId + ":" + userId;

        String lockKey = RedisPolicy.QUIZ_VOTE_LOCK_PREFIX + quizId;
        RLock lock = redissonClient.getLock(lockKey);
        try {
            if (lock.tryLock(2, 5, TimeUnit.SECONDS)) {
                if (redisDao.existData(redisKey)) {
                    throw new SurveyException(ErrorCode.ALREADY_VOTED);
                }

                Survey survey = surveyRepository.findById(quizId)
                        .orElseThrow(() -> new SurveyException(ErrorCode.QUIZ_NOT_FOUND));
                String choice = request.getChoice();
                switch (choice) {
                    case "A" -> survey.setVoteA(survey.getVoteA() + 1);
                    case "B" -> survey.setVoteB(survey.getVoteB() + 1);
                    case "C" -> survey.setVoteB(survey.getVoteC() + 1);
                    default -> throw new SurveyException(ErrorCode.INVALID_INPUT_VALUE);
                }
                surveyRepository.save(survey);
                redisDao.setValues(redisKey, choice, Duration.ofDays(1));

                return SurveyVoteResponseDto.of(
                        quizId, choice,
                        survey.getVoteA(), survey.getVoteB(), survey.getVoteC()
                );
            } else {
                throw new SurveyException(ErrorCode.FAIL_TO_GET_LOCK);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SurveyException(ErrorCode.INTERNAL_SERVER_ERROR);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}

