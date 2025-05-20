package com.littlebank.finance.domain.challenge.service;

import com.littlebank.finance.domain.challenge.domain.Challenge;
import com.littlebank.finance.domain.challenge.domain.ChallengeCategory;
import com.littlebank.finance.domain.challenge.domain.ChallengeParticipation;
import com.littlebank.finance.domain.challenge.domain.Status;
import com.littlebank.finance.domain.challenge.domain.repository.ChallengeParticipationRepository;
import com.littlebank.finance.domain.challenge.domain.repository.ChallengeRepository;
import com.littlebank.finance.domain.challenge.dto.request.ChallengeAdminRequestDto;
import com.littlebank.finance.domain.challenge.dto.request.ChallengeUserRequestDto;
import com.littlebank.finance.domain.challenge.dto.response.ChallengeAdminResponseDto;
import com.littlebank.finance.domain.challenge.dto.response.ChallengeUserResponseDto;
import com.littlebank.finance.domain.challenge.exception.ChallengeException;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.error.exception.ErrorCode;
import com.littlebank.finance.global.redis.RedisPolicy;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Transactional
@Service
@RequiredArgsConstructor
public class ChallengeService {
    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final ChallengeParticipationRepository participationRepository;
    private final RedissonClient redissonClient;

    // ADMIN
    public ChallengeAdminResponseDto createChallenge(Long userId, ChallengeAdminRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ChallengeException(ErrorCode.USER_NOT_FOUND));
        Challenge challenge = Challenge.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .category(ChallengeCategory.valueOf(dto.getCategory()))
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .totalStudyTime(dto.getTotalStudyTime())
                .totalParticipants(dto.getTotalParticipants())
                .currentParticipants(0)
                .build();
        Challenge savedChallenge = challengeRepository.save(challenge);
        return ChallengeAdminResponseDto.of(savedChallenge, 0);
    }


    // USER
    public ChallengeUserResponseDto joinChallenge(Long userId, Long challengeId, ChallengeUserRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ChallengeException(ErrorCode.USER_NOT_FOUND));

        //redisson
        String lockKey = RedisPolicy.CHALLENGE_JOIN_KEY_PREFIX + challengeId;
        RLock lock = redissonClient.getLock(lockKey);

        String countKey = RedisPolicy.CHALLENGE_CURRENT_COUNT_KEY_PREFIX + challengeId;
        boolean isLocked = false;
        try {
            isLocked = lock.tryLock(5, 3, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new ChallengeException(ErrorCode.FAIL_TO_GET_LOCK);
            }
            Challenge challenge = challengeRepository.findById(challengeId)
                    .orElseThrow(() -> new ChallengeException(ErrorCode.CHALLENGE_NOT_FOUND));

            if (participationRepository.existsByChallengeIdAndUserId(challengeId, userId)) {
                throw new ChallengeException(ErrorCode.ALREADY_JOINED);
            }

            if (request.getStartDate().isBefore(challenge.getStartDate()) || request.getEndDate().isAfter(challenge.getEndDate())) {
                throw new ChallengeException(ErrorCode.INVALID_PARTICIPATION_PERIOD);
            }

            RAtomicLong counter = redissonClient.getAtomicLong(countKey);

            if (counter.isExists() == false) {
                counter.set(challenge.getCurrentParticipants());
            }
            long current = counter.incrementAndGet();
            if (current > challenge.getTotalParticipants()) {
                counter.decrementAndGet();
                throw new ChallengeException(ErrorCode.CHALLENGE_FULL);
            }


            LocalDateTime now = LocalDateTime.now();
            LocalDateTime userStartDateTime = LocalDateTime.of(request.getStartDate(), request.getStartTime());

            Status status;
            if (now.isBefore(userStartDateTime)) {
                status = Status.BEFORE;
            } else if (now.isAfter(request.getEndDate().atTime(23, 59, 59))) {
                status = Status.FINISHED;
            } else {
                status = Status.IN_PROGRESS;
            }

            ChallengeParticipation participation = ChallengeParticipation.builder()
                    .challenge(challenge)
                    .user(user)
                    .status(status)
                    .startDate(request.getStartDate())
                    .endDate(request.getEndDate())
                    .subject(request.getSubject())
                    .startTime(request.getStartTime())
                    .totalStudyTime(request.getTotalStudyTime())
                    .reward(request.getReward())
                    .isDeleted(false)
                    .build();

            participationRepository.save(participation);
            challenge.setCurrentParticipants(challenge.getCurrentParticipants()+ 1);
            challengeRepository.save(challenge);
            return ChallengeUserResponseDto.of(participation);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ChallengeException(ErrorCode.LOCK_INTERUPPTED);
        } finally {
            if (isLocked) {
                lock.unlock();
            }
        }
    }
    @Transactional(readOnly = true)
    public Page<ChallengeAdminResponseDto> getChallenges(Long userId, ChallengeCategory challengeCategory, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        Page<Challenge> challenges;
        if (challengeCategory != null) {
            challenges = challengeRepository.findByCategoryAndIsDeletedFalse(challengeCategory, pageable);
        } else {
            challenges = challengeRepository.findByIsDeletedFalse(pageable);
        }
        return challenges.map(challenge -> {
            int currentActiveParticipants = participationRepository.countByChallengeIdAndStatuses(
                    challenge.getId(), List.of(Status.BEFORE, Status.IN_PROGRESS)
            );
            return ChallengeAdminResponseDto.of(challenge, currentActiveParticipants);
        });

    }

    public ChallengeAdminResponseDto getChallengeDetail(Long userId, Long challengeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ChallengeException(ErrorCode.CHALLENGE_NOT_FOUND));

        challenge.increaseViewCount();
        int currentActiveParticipants = participationRepository.countByChallengeIdAndStatuses(
                challengeId, List.of(Status.BEFORE, Status.IN_PROGRESS)
        );
        return ChallengeAdminResponseDto.of(challenge, currentActiveParticipants);
    }
}
