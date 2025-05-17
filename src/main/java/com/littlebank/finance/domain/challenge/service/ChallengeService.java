package com.littlebank.finance.domain.challenge.service;

import com.littlebank.finance.domain.challenge.domain.Challenge;
import com.littlebank.finance.domain.challenge.domain.ChallengeCategory;
import com.littlebank.finance.domain.challenge.domain.ChallengeParticipation;
import com.littlebank.finance.domain.challenge.domain.repository.ChallengeRepository;
import com.littlebank.finance.domain.challenge.dto.request.ChallengeCreateRequestDto;
import com.littlebank.finance.domain.challenge.dto.response.ChallengeResponseDto;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChallengeService {
    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    public ChallengeResponseDto createChallenge(Long userId, ChallengeCreateRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        Challenge challenge = Challenge.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .category(ChallengeCategory.valueOf(dto.getCategory()))
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .totalStudyTime(dto.getTotalStudyTime())
                .maxParticipants(dto.getMaxParticipants())
                .preferredStartTime(dto.getPreferredStartTime())
                .preferredReward(dto.getPreferredReward())
                .build();
        Challenge savedChallenge = challengeRepository.save(challenge);
        ChallengeParticipation challengeParticipation = ChallengeParticipation.builder()
                .challenge(savedChallenge)
                .user(user)
                .build();
        return ChallengeResponseDto.of(savedChallenge);
    }
}
