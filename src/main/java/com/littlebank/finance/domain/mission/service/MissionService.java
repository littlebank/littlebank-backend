package com.littlebank.finance.domain.mission.service;


import com.littlebank.finance.domain.family.domain.FamilyMember;
import com.littlebank.finance.domain.family.domain.Status;
import com.littlebank.finance.domain.family.domain.repository.FamilyMemberRepository;
import com.littlebank.finance.domain.mission.domain.*;
import com.littlebank.finance.domain.mission.domain.repository.MissionRepository;
import com.littlebank.finance.domain.mission.dto.request.CreateMissionRequestDto;
import com.littlebank.finance.domain.mission.dto.request.MissionRecentRewardRequestDto;
import com.littlebank.finance.domain.mission.dto.response.CommonMissionResponseDto;
import com.littlebank.finance.domain.mission.dto.response.MissionRecentRewardResponseDto;
import com.littlebank.finance.domain.mission.exception.MissionException;
import com.littlebank.finance.domain.notification.domain.Notification;
import com.littlebank.finance.domain.notification.domain.NotificationType;
import com.littlebank.finance.domain.notification.domain.repository.NotificationRepository;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.common.CustomPageResponse;
import com.littlebank.finance.global.common.PaginationPolicy;
import com.littlebank.finance.global.error.exception.ErrorCode;
import com.littlebank.finance.global.firebase.FirebaseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class MissionService {
    private final UserRepository userRepository;
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

            // 알림 생성
            try {
                Notification notification = notificationRepository.save(Notification.builder()
                        .receiver(child)
                        .message("우리 부모님이 미션 신청을 했습니다!")
                        .type(NotificationType.MISSION_PROPOSAL)
                        .targetId(mission.getId())
                        .isRead(false)
                        .build());
                firebaseService.sendNotification(notification);
            } catch (DataIntegrityViolationException e) {
                log.warn("이미 동일한 알림이 존재합니다.");
            }
        }

        return responses;
    }

    public MissionRecentRewardResponseDto getRecentReward(MissionRecentRewardRequestDto request, Long childId) {
        FamilyMember childMember = familyMemberRepository.findByUserIdAndStatusWithUser(childId, Status.JOINED)
                .orElseThrow(() -> new UserException(ErrorCode.FAMILY_MEMBER_NOT_FOUND));
        Integer recentReward = missionRepository.findRecentReward(childId, MissionType.FAMILY, request.getCategory(), request.getSubject());

        MissionRecentRewardResponseDto response = MissionRecentRewardResponseDto.builder()
                .type(MissionType.FAMILY)
                .category(request.getCategory())
                .subject(request.getSubject())
                .recentReward(recentReward)
                .build();
        return response;
    }
    public CommonMissionResponseDto acceptMission(Long missionId) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new UserException(ErrorCode.MISSION_NOT_FOUND));
        if (mission.getEndDate().isBefore(LocalDateTime.now())) {
            throw new MissionException(ErrorCode.MISSION_END_DATE_EXPIRED);
        }
        mission.acceptProposal();
        return CommonMissionResponseDto.of(mission);
    }

    public CustomPageResponse<CommonMissionResponseDto> getMyMissions(Long userId, int page) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        Pageable pageable = PageRequest.of(page,
                PaginationPolicy.CHALLENGE_LIST_PAGE_SIZE,
                Sort.by(Sort.Direction.DESC, "createdDate")
        );
        Page<Mission> missions = missionRepository.findByChild(user, pageable);
        Page<CommonMissionResponseDto> responsePage = missions.map(CommonMissionResponseDto::of);
        return CustomPageResponse.of(responsePage);
    }

    public CustomPageResponse<CommonMissionResponseDto> getChildMissions(Long childId, int page) {
        FamilyMember childMember = familyMemberRepository.findByUserIdAndStatusWithUser(childId, Status.JOINED)
                .orElseThrow(() -> new UserException(ErrorCode.FAMILY_MEMBER_NOT_FOUND));
        Pageable pageable = PageRequest.of(page,
                PaginationPolicy.CHALLENGE_LIST_PAGE_SIZE,
                Sort.by(Sort.Direction.DESC, "createdDate")
        );
        Page<Mission> missions = missionRepository.findByChild(childMember.getUser(), pageable);
        Page<CommonMissionResponseDto> responsePage = missions.map(CommonMissionResponseDto::of);
        return CustomPageResponse.of(responsePage);
    }
}
