package com.littlebank.finance.domain.quiz.service.admin;


import com.littlebank.finance.domain.quiz.domain.Quiz;
import com.littlebank.finance.domain.quiz.domain.repository.QuizRepository;
import com.littlebank.finance.domain.quiz.dto.request.CreateQuizRequestDto;
import com.littlebank.finance.domain.quiz.dto.response.CreateQuizResponseDto;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.domain.user.service.UserService;
import com.littlebank.finance.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdminQuizService {
    private final UserRepository userRepository;
    private final QuizRepository quizRepository;

    public CreateQuizResponseDto createQuiz(Long adminId, CreateQuizRequestDto request) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        Quiz quiz = quizRepository.save(request.toQuiz());
        return CreateQuizResponseDto.of(quiz);
    }
}
