package com.littlebank.finance.domain.challenge.service.admin;

import com.littlebank.finance.domain.challenge.domain.Challenge;
import com.littlebank.finance.domain.challenge.domain.ChallengeCategory;
import com.littlebank.finance.domain.challenge.domain.repository.ChallengeRepository;
import com.littlebank.finance.domain.challenge.dto.request.admin.ChallengeAdminRequestDto;
import com.littlebank.finance.domain.challenge.dto.response.admin.ChallengeAdminResponseDto;
import com.littlebank.finance.domain.challenge.exception.ChallengeException;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class ChallengeAdminService {
    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
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
}
