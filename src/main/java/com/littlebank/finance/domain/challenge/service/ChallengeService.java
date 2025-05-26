package com.littlebank.finance.domain.challenge.service;

import com.littlebank.finance.domain.challenge.domain.Challenge;
import com.littlebank.finance.domain.challenge.domain.ChallengeCategory;
import com.littlebank.finance.domain.challenge.domain.ChallengeParticipation;
import com.littlebank.finance.domain.challenge.domain.ChallengeStatus;
import com.littlebank.finance.domain.challenge.domain.repository.ChallengeParticipationRepository;
import com.littlebank.finance.domain.challenge.domain.repository.ChallengeRepository;
import com.littlebank.finance.domain.challenge.dto.request.ChallengeUserRequestDto;
import com.littlebank.finance.domain.challenge.dto.response.admin.ChallengeAdminResponseDto;
import com.littlebank.finance.domain.challenge.dto.response.ChallengeUserResponseDto;
import com.littlebank.finance.domain.challenge.exception.ChallengeException;
import com.littlebank.finance.domain.family.domain.repository.FamilyMemberRepository;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.common.CustomPageResponse;
import com.littlebank.finance.global.common.PaginationPolicy;
import com.littlebank.finance.global.error.exception.ErrorCode;
import com.littlebank.finance.global.redis.RedisPolicy;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.*;
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
    private final ChallengeParticipationRepository challengeParticipationRepository;


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

            ChallengeStatus challengeStatus;
            if (now.isBefore(userStartDateTime)) {
                challengeStatus = ChallengeStatus.BEFORE;
            } else if (now.isAfter(request.getEndDate().atTime(23, 59, 59))) {
                challengeStatus = ChallengeStatus.FINISHED;
            } else {
                challengeStatus = ChallengeStatus.IN_PROGRESS;
            }

            ChallengeParticipation participation = ChallengeParticipation.builder()
                    .challenge(challenge)
                    .user(user)
                    .challengeStatus(challengeStatus)
                    .startDate(request.getStartDate())
                    .endDate(request.getEndDate())
                    .subject(request.getSubject())
                    .startTime(request.getStartTime())
                    .totalStudyTime(request.getTotalStudyTime())
                    .reward(request.getReward())
                    .isDeleted(false)
                    .build();

            participationRepository.save(participation);
            challenge.setCurrentParticipants(challenge.getCurrentParticipants() + 1);
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
    public CustomPageResponse<ChallengeAdminResponseDto> getChallenges(Long userId, ChallengeCategory challengeCategory, int page) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        Pageable pageable = PageRequest.of(page,
                PaginationPolicy.CHALLENGE_LIST_PAGE_SIZE,
                Sort.by(Sort.Direction.DESC, "createdDate")
        );

        Page<Challenge> challenges;
        if (challengeCategory != null) {
            challenges = challengeRepository.findByCategoryAndIsDeletedFalse(challengeCategory, pageable);
        } else {
            challenges = challengeRepository.findByIsDeletedFalse(pageable);
        }

        List<ChallengeAdminResponseDto> responseList = challenges.stream()
                .map(challenge -> {
                    int currentActiveParticipants = participationRepository.countByChallengeIdAndStatuses(
                            challenge.getId(), List.of(ChallengeStatus.BEFORE, ChallengeStatus.IN_PROGRESS)
                    );
                    return ChallengeAdminResponseDto.of(challenge, currentActiveParticipants);
                })
                .toList();
        Page<ChallengeAdminResponseDto> responsePage = new PageImpl<>(responseList, pageable, challenges.getTotalElements());
        return CustomPageResponse.of(responsePage);

    }

    public ChallengeAdminResponseDto getChallengeDetail(Long userId, Long challengeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ChallengeException(ErrorCode.CHALLENGE_NOT_FOUND));

        challenge.increaseViewCount();
        int currentActiveParticipants = participationRepository.countByChallengeIdAndStatuses(
                challengeId, List.of(ChallengeStatus.BEFORE, ChallengeStatus.IN_PROGRESS)
        );
        return ChallengeAdminResponseDto.of(challenge, currentActiveParticipants);
    }

    public CustomPageResponse<ChallengeAdminResponseDto> getMyChallenges(Long userId, ChallengeStatus challengeStatus, int page) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        Pageable pageable = PageRequest.of(page,
                PaginationPolicy.CHALLENGE_LIST_PAGE_SIZE,
                Sort.by(Sort.Direction.DESC, "createdDate")
        );

        List<ChallengeStatus> filterStatuses;
        if (challengeStatus == ChallengeStatus.BEFORE || challengeStatus == ChallengeStatus.IN_PROGRESS) {
            filterStatuses = List.of(ChallengeStatus.BEFORE, ChallengeStatus.IN_PROGRESS);
        } else if (challengeStatus == ChallengeStatus.FINISHED) {
            filterStatuses = List.of(ChallengeStatus.FINISHED);
        } else {
            throw new ChallengeException(ErrorCode.INVALID_CHALLENGE_CATEGORY);
        }

        Page<ChallengeParticipation> participations = participationRepository
                .findMyValidParticipations(user.getId(), filterStatuses, pageable);

        List<ChallengeAdminResponseDto> challengeList = participations.stream()
                .map(participation -> {
                    Challenge challenge = participation.getChallenge();
                    int current = participationRepository.countByChallengeIdAndStatuses(
                            challenge.getId(),
                            List.of(ChallengeStatus.BEFORE, ChallengeStatus.IN_PROGRESS)
                    );
                    return ChallengeAdminResponseDto.of(challenge, current);
                })
                .toList();

        Page<ChallengeAdminResponseDto> responsePage = new PageImpl<>(challengeList, pageable, participations.getTotalElements());
        return CustomPageResponse.of(responsePage);

    }

    public List<ChallengeUserResponseDto> getChildChallenge(Long familyId) {
        List<ChallengeParticipation> participations = challengeParticipationRepository.findChildrenParticipationByFamilyId
                (familyId, List.of(ChallengeStatus.BEFORE, ChallengeStatus.IN_PROGRESS));
        return participations.stream().map(ChallengeUserResponseDto::of).toList();
    }
}