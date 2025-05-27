package com.littlebank.finance.domain.goal.service;

import com.littlebank.finance.domain.family.domain.Family;
import com.littlebank.finance.domain.family.domain.FamilyMember;
import com.littlebank.finance.domain.family.domain.repository.FamilyMemberRepository;
import com.littlebank.finance.domain.family.domain.repository.FamilyRepository;
import com.littlebank.finance.domain.family.exception.FamilyException;
import com.littlebank.finance.domain.goal.domain.Goal;
import com.littlebank.finance.domain.goal.domain.GoalCategory;
import com.littlebank.finance.domain.goal.domain.repository.GoalRepository;
import com.littlebank.finance.domain.goal.dto.request.GoalApplyRequest;
import com.littlebank.finance.domain.goal.dto.response.ChildGoalResponse;
import com.littlebank.finance.domain.goal.dto.response.P3CommonGoalResponse;
import com.littlebank.finance.domain.goal.dto.response.StampCheckResponse;
import com.littlebank.finance.domain.goal.dto.response.WeeklyGoalResponse;
import com.littlebank.finance.domain.goal.exception.GoalException;
import com.littlebank.finance.domain.notification.domain.Notification;
import com.littlebank.finance.domain.notification.domain.NotificationType;
import com.littlebank.finance.domain.notification.domain.repository.NotificationRepository;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.error.exception.ErrorCode;
import com.littlebank.finance.global.firebase.FirebaseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Calendar.*;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class GoalService {
    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
    private final FamilyMemberRepository familyMemberRepository;
    private final GoalRepository goalRepository;
    private final NotificationRepository notificationRepository;
    private final FirebaseService firebaseService;

    public P3CommonGoalResponse applyGoal(Long userId, GoalApplyRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        verifyDuplicateGoalCategory(user, request.getCategory());

        Family family = familyRepository.findByUserIdWithMember(userId)
                .orElseThrow(() -> new FamilyException(ErrorCode.FAMILY_NOT_FOUND));

        Goal goal = goalRepository.save(request.toEntity(user, family));

        List<FamilyMember> parents = familyMemberRepository.findParentsByFamilyId(family.getId());
        try {
            parents.stream()
                    .forEach(p -> {
                        Notification notification = notificationRepository.save(Notification.builder()
                                .receiver(p.getUser())
                                .message("우리 예쁜 " + family.getMembers().get(0).getNickname() + "(이)가 목표를 신청했어요!")
                                .type(NotificationType.GOAL_PROPOSAL)
                                .targetId(goal.getId())
                                .isRead(false)
                                .build());
                        firebaseService.sendNotification(notification);
                    });
        } catch (DataIntegrityViolationException e) {
            log.warn("이미 동일한 알림이 존재합니다.");
        }

        return P3CommonGoalResponse.of(goal);
    }

    @Transactional(readOnly = true)
    public List<WeeklyGoalResponse> getWeeklyGoal(Long userId) {
        List<Goal> results = goalRepository.findByCreatedByAndWeekly(userId);

        return results.stream()
                .map(g -> WeeklyGoalResponse.of(g))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ChildGoalResponse> getChildWeeklyGoal(Long familyId) {
        return goalRepository.findChildWeeklyGoalResponses(familyId);
    }

    public P3CommonGoalResponse acceptApplyGoal(Long targetGoalId) {
        Goal goal = goalRepository.findById(targetGoalId)
                .orElseThrow(() -> new GoalException(ErrorCode.GOAL_NOT_FOUND));

        if (goal.getEndDate().isBefore(LocalDateTime.now())) {
            throw new GoalException(ErrorCode.GOAL_END_DATE_EXPIRED);
        }

        goal.acceptProposal();

        return P3CommonGoalResponse.of(goal);
    }

    @Transactional(readOnly = true)
    public List<ChildGoalResponse> getChildGoals(Long familyId) {
        return goalRepository.findAllChildGoalResponses(familyId);
    }

    public P3CommonGoalResponse checkGoal(Long goalId, Integer day) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new GoalException(ErrorCode.GOAL_NOT_FOUND));

        switch (day % 7 + 1) {
            case MONDAY -> goal.checkMon(Boolean.TRUE);
            case TUESDAY -> goal.checkTue(Boolean.TRUE);
            case WEDNESDAY -> goal.checkWed(Boolean.TRUE);
            case THURSDAY -> goal.checkThu(Boolean.TRUE);
            case FRIDAY -> goal.checkFri(Boolean.TRUE);
            case SATURDAY -> goal.checkSat(Boolean.TRUE);
            case SUNDAY -> goal.checkSun(Boolean.TRUE);
        }

        return P3CommonGoalResponse.of(goal);
    }

    @Transactional(readOnly = true)
    public StampCheckResponse checkStamp(Long goalId) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new GoalException(ErrorCode.GOAL_NOT_FOUND));

        return StampCheckResponse.of(goal);
    }

    private void verifyDuplicateGoalCategory(User user, GoalCategory category) {
        if (goalRepository.existsCategoryAndWeekly(user.getId(), category)) {
            throw new GoalException(ErrorCode.GOAL_WEEKLY_DUPLICATE);
        }
    }
}
