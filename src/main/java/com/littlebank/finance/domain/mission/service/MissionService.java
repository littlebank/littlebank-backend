package com.littlebank.finance.domain.mission.service;


import com.littlebank.finance.domain.family.domain.FamilyMember;
import com.littlebank.finance.domain.family.domain.Status;
import com.littlebank.finance.domain.family.domain.repository.FamilyMemberRepository;
import com.littlebank.finance.domain.friend.domain.repository.FriendRepository;
import com.littlebank.finance.domain.friend.dto.response.FriendInfoResponse;
import com.littlebank.finance.domain.mission.domain.*;
import com.littlebank.finance.domain.mission.domain.repository.MissionRepository;
import com.littlebank.finance.domain.mission.dto.request.CreateMissionRequestDto;
import com.littlebank.finance.domain.mission.dto.response.*;

import com.littlebank.finance.domain.mission.dto.request.MissionRecentRewardRequestDto;
import com.littlebank.finance.domain.mission.dto.response.CommonMissionResponseDto;
import com.littlebank.finance.domain.mission.dto.response.MissionRecentRewardResponseDto;
import com.littlebank.finance.domain.mission.dto.response.*;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    private final FriendRepository friendRepository;

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
                PaginationPolicy.MISSION_LIST_PAGE_SIZE,
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
    public List<MissionRankingResponseDto> getFriendRanking(Long userId, RankingRange range, int page) {
        LocalDate today = LocalDate.now();
        LocalDateTime startDay = range.calculateStartDay(today);
        LocalDateTime endDay = range.calculateEndDay(today);

        List<FriendInfoResponse> friends = friendRepository.findFriendsByUserId(userId);
        List<Long> friendIds = friends.stream().map(f -> f.getUserInfo().getUserId()).toList();
        List<MissionStatDto> stats = missionRepository.getMissionStatsByPeriod(friendIds, startDay, endDay);

        Map<Long, List<MissionStatDto>> grouped = stats.stream()
                .collect(Collectors.groupingBy(MissionStatDto::getChildId));

        return friends.stream().map(friend -> {
                    Long friendUserId = friend.getUserInfo().getUserId();
                    Long friendId = friend.getFriendInfo().getFriendId();
                    String friendName = friend.getFriendInfo().getCustomName();
                    boolean isBestFriend = friend.getFriendInfo().getIsBestFriend();

                    List<MissionStatDto> userStats = grouped.getOrDefault(friendUserId, List.of());

                    // 누적 계산용 Map
                    Map<MissionSubject, int[]> learningStatMap = new EnumMap<>(MissionSubject.class);

                    int habitTotal = 0;
                    int habitCompleted = 0;
                    int total = 0;
                    int completed = 0;

                    for (MissionStatDto s : userStats) {
                        int count = s.getCount();
                        boolean isCountable = s.getStatus() == MissionStatus.ACHIEVEMENT || s.getStatus() == MissionStatus.ACCEPT;
                        boolean isCompleted = s.getStatus() == MissionStatus.ACHIEVEMENT;
                        if (!isCountable) continue;

                        if (s.getCategory() == MissionCategory.LEARNING && s.getSubject() != null) {
                            learningStatMap.putIfAbsent(s.getSubject(), new int[]{0, 0}); // [total, completed]
                            learningStatMap.get(s.getSubject())[0] += count;
                            if (s.getStatus() == MissionStatus.ACHIEVEMENT) {
                                learningStatMap.get(s.getSubject())[1] += count;
                            }
                        } else if (s.getCategory() == MissionCategory.HABIT) {
                            habitTotal += count;
                            if (isCompleted) habitCompleted += count;
                        }

                        total += count;
                        if (isCompleted) completed += count;
                    }

                    List<LearningMissionStats> learningStats = learningStatMap.entrySet().stream()
                            .map(entry -> {
                                int subjectTotal = entry.getValue()[0];
                                int subjectCompleted = entry.getValue()[1];
                                double subjectRate = subjectTotal == 0 ? 0.0 : Math.round((subjectCompleted * 100.0 / subjectTotal) * 100) / 100.0;

                                return LearningMissionStats.of(entry.getKey(), subjectTotal, subjectCompleted, subjectRate);
                            }).toList();

                    double totalRate = total == 0 ? 0.0 : Math.round((completed * 100.0 / total) * 100) / 100.0;
                    double habitRate = habitTotal == 0 ? 0.0 : Math.round((habitCompleted * 100.0 / habitTotal) * 100) / 100.0;
                    return MissionRankingResponseDto.of(friendUserId, friendId, friendName, isBestFriend, total, completed, totalRate, learningStats, habitTotal, habitCompleted, habitRate);

                }).sorted(Comparator.comparingDouble(MissionRankingResponseDto::getCompletionRate).reversed())
                .toList();
    }
}