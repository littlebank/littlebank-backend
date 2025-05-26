package com.littlebank.finance.domain.goal.service;

import com.littlebank.finance.domain.family.domain.Family;
import com.littlebank.finance.domain.family.domain.repository.FamilyRepository;
import com.littlebank.finance.domain.family.exception.FamilyException;
import com.littlebank.finance.domain.goal.domain.Goal;
import com.littlebank.finance.domain.goal.domain.GoalCategory;
import com.littlebank.finance.domain.goal.domain.repository.GoalRepository;
import com.littlebank.finance.domain.goal.dto.request.GoalApplyRequest;
import com.littlebank.finance.domain.goal.dto.response.ChildGoalResponse;
import com.littlebank.finance.domain.goal.dto.response.P3CommonGoalResponse;
import com.littlebank.finance.domain.goal.dto.response.WeeklyGoalResponse;
import com.littlebank.finance.domain.goal.exception.GoalException;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.common.CustomPageResponse;
import com.littlebank.finance.global.error.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class GoalService {
    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
    private final GoalRepository goalRepository;

    public P3CommonGoalResponse applyGoal(Long userId, GoalApplyRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        verifyDuplicateGoalCategory(user, request.getCategory());

        Family family = familyRepository.findById(userId)
                .orElseThrow(() -> new FamilyException(ErrorCode.FAMILY_NOT_FOUND));

        Goal goal = goalRepository.save(request.toEntity(user, family));

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
    public CustomPageResponse<ChildGoalResponse> getChildGoals(Long familyId, Pageable pageable) {
        return CustomPageResponse.of(goalRepository.findAllChildGoalResponses(familyId, pageable));
    }

    private void verifyDuplicateGoalCategory(User user, GoalCategory category) {
        if (goalRepository.existsCategoryAndWeekly(user.getId(), category)) {
            throw new GoalException(ErrorCode.GOAL_WEEKLY_DUPLICATE);
        }
    }
}
