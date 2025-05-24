package com.littlebank.finance.domain.goal.service;

import com.littlebank.finance.domain.family.domain.Family;
import com.littlebank.finance.domain.family.domain.repository.FamilyRepository;
import com.littlebank.finance.domain.family.exception.FamilyException;
import com.littlebank.finance.domain.goal.domain.Goal;
import com.littlebank.finance.domain.goal.domain.repository.GoalRepository;
import com.littlebank.finance.domain.goal.dto.request.GoalApplyRequest;
import com.littlebank.finance.domain.goal.dto.response.GoalApplyResponse;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.error.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class GoalService {
    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
    private final GoalRepository goalRepository;

    public GoalApplyResponse applyGoal(Long userId, GoalApplyRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        Family family = familyRepository.findById(userId)
                .orElseThrow(() -> new FamilyException(ErrorCode.FAMILY_NOT_FOUND));

        Goal goal = goalRepository.save(request.toEntity(user, family));

        return GoalApplyResponse.of(goal);
    }
}
