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
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final RedisDao redisDao;
    private final RedissonClient redissonClient;

    public SurveyVoteResponseDto joinSurvey(Long userId, Long surveyId, SurveyVoteRequestDto request) {
        String redisKey = RedisPolicy.SURVEY_VOTE_PREFIX + surveyId + ":" + userId;
        String lockKey = RedisPolicy.SURVEY_VOTE_LOCK_PREFIX + surveyId;
        RLock lock = redissonClient.getLock(lockKey);
        try {
            if (!lock.tryLock(2, 5, TimeUnit.SECONDS)) {
                throw new SurveyException(ErrorCode.FAIL_TO_GET_LOCK);
            }
            if (redisDao.existData(redisKey)) {
                throw new SurveyException(ErrorCode.ALREADY_VOTED);
            }
            Survey survey = surveyRepository.findById(surveyId)
                    .orElseThrow(() -> new SurveyException(ErrorCode.SURVEY_NOT_FOUND));
            increaseVote(survey, request.getChoice());
            surveyRepository.save(survey);
            redisDao.setValues(redisKey, request.getChoice(), Duration.ofDays(1));

            return SurveyVoteResponseDto.ofWithResult(survey, request.getChoice());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SurveyException(ErrorCode.INTERNAL_SERVER_ERROR);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private void increaseVote(Survey survey, String choice) {
        switch (choice) {
            case "A" -> survey.setVoteA(survey.getVoteA() + 1);
            case "B" -> survey.setVoteB(survey.getVoteB() + 1);
            case "C" -> survey.setVoteC(survey.getVoteC() + 1);
            default -> throw new SurveyException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }

    public SurveyVoteResponseDto showSurvey(Long userId) {
        List<Survey> allSurveys = surveyRepository.findAllByIsDeletedFalse();
        long seed = LocalDate.now().toEpochDay();
        Collections.shuffle(allSurveys, new Random(seed));

        Survey survey = allSurveys.get(0);
        String redisKey = RedisPolicy.SURVEY_VOTE_PREFIX + survey.getId() + ":" + userId;

        if (redisDao.existData(redisKey)) {
            String choice = redisDao.getValues(redisKey);
            return SurveyVoteResponseDto.ofWithResult(survey, choice);
        } else {
            return SurveyVoteResponseDto.ofWithoutResult(survey);
        }
    }
}

