package com.littlebank.finance.domain.mission.service;


import com.littlebank.finance.domain.family.domain.FamilyMember;
import com.littlebank.finance.domain.family.domain.Status;
import com.littlebank.finance.domain.family.domain.repository.FamilyMemberRepository;
import com.littlebank.finance.domain.family.domain.repository.FamilyRepository;
import com.littlebank.finance.domain.mission.domain.Mission;
import com.littlebank.finance.domain.mission.domain.MissionStatus;
import com.littlebank.finance.domain.mission.domain.repository.MissionRepository;
import com.littlebank.finance.domain.mission.dto.request.CreateMissionRequestDto;
import com.littlebank.finance.domain.mission.dto.response.CommonMissionResponseDto;
import com.littlebank.finance.domain.mission.dto.response.MissionRecentRewardResponseDto;
import com.littlebank.finance.domain.notification.domain.repository.NotificationRepository;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.error.exception.ErrorCode;
import com.littlebank.finance.global.firebase.FirebaseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class MissionService {
    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
    private final MissionRepository missionRepository;
    private final NotificationRepository notificationRepository;
    private final FirebaseService firebaseService;
    private final FamilyMemberRepository familyMemberRepository;

    public List<CommonMissionResponseDto> createMission(CreateMissionRequestDto request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        List<CommonMissionResponseDto> responses = new ArrayList<>();
        for (User child : request.getChilds()) {
            Mission mission = missionRepository.save(request.toEntity(user, child));
            responses.add(CommonMissionResponseDto.of(mission));
        }
        return responses;
    }

    public MissionRecentRewardResponseDto getRecentReward(Long childId) {
        FamilyMember childMember = familyMemberRepository.findByUserIdAndStatusWithUser(childId, Status.JOINED)
                .orElseThrow(() -> new UserException(ErrorCode.FAMILY_MEMBER_NOT_FOUND));
        Integer recentReward = missionRepository.findTopByChildIdAndStatusOrderByEndDateDesc(childId, MissionStatus.ACHIEVEMENT)
                .map(Mission::getReward)
                .orElse(0);
        return new MissionRecentRewardResponseDto(recentReward);
    }
}
