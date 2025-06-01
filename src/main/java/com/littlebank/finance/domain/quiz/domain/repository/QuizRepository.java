package com.littlebank.finance.domain.quiz.domain.repository;

import com.littlebank.finance.domain.quiz.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
