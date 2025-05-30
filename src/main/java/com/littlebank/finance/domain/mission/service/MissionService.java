package com.littlebank.finance.domain.mission.service;


import com.littlebank.finance.domain.family.domain.Family;
import com.littlebank.finance.domain.family.domain.repository.FamilyRepository;
import com.littlebank.finance.domain.family.exception.FamilyException;
import com.littlebank.finance.domain.mission.domain.Mission;
import com.littlebank.finance.domain.mission.domain.MissionAssignment;
import com.littlebank.finance.domain.mission.domain.MissionType;
import com.littlebank.finance.domain.mission.domain.repository.AssignmentRepository;
import com.littlebank.finance.domain.mission.domain.repository.MissionRepository;
import com.littlebank.finance.domain.mission.dto.request.CreateMissionRequestDto;
import com.littlebank.finance.domain.mission.dto.response.CreateMissionResponseDto;
import com.littlebank.finance.domain.notification.domain.Notification;
import com.littlebank.finance.domain.notification.domain.NotificationType;
import com.littlebank.finance.domain.notification.domain.repository.NotificationRepository;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.error.exception.ErrorCode;
import com.littlebank.finance.global.firebase.FirebaseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//private MissionType resolveMissionType(User creator) {
//    return switch (creator.getRole()) {
//        case PARENT -> MissionType.FAMILY;
//        case TEACHER -> MissionType.ACADEMY;
//        default -> throw new IllegalArgumentException("미션 생성 권한 없음");
//    };
//}
@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class MissionService {
    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
    private final MissionRepository missionRepository;
    private final AssignmentRepository assignmentRepository;
    private final NotificationRepository notificationRepository;
    private final FirebaseService firebaseService;

    public CreateMissionResponseDto createMission(CreateMissionRequestDto request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        Family family = familyRepository.findByUserIdWithMember(userId)
                .orElseThrow(() -> new FamilyException(ErrorCode.FAMILY_NOT_FOUND));

        MissionType type;
        if (user.getRole().equals("PARENT")) {
            type = MissionType.FAMILY;
        } else {
            type = MissionType.ACADEMY;
        }

        Mission mission = missionRepository.save(request.toEntity(user, type));
        List<Long> childIds = request.getChildIds();

        for (Long childId : childIds) {
            User child = userRepository.findById(childId)
                    .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
            MissionAssignment assignment = MissionAssignment.builder()
                    .mission(mission)
                    .child(child)
                    .build();
            assignmentRepository.save(assignment);
            String message = family.getMembers().get(0).getNickname() + "님이 미션을 줬어요!";
            Notification notification = notificationRepository.save(Notification.builder()
                    .receiver(child)
                    .message(message)
                    .type(NotificationType.GOAL_PROPOSAL)
                    .targetId(mission.getId())
                    .isRead(false)
                    .build());
            firebaseService.sendNotification(notification);
        }
        return CreateMissionResponseDto.of(mission);
    }
}
