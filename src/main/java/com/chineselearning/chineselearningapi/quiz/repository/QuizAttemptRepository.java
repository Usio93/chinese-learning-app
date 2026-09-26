package com.chineselearning.chineselearningapi.quiz.repository;

import com.chineselearning.chineselearningapi.quiz.entity.QuizAttempt;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizAttemptRepository
        extends JpaRepository<QuizAttempt, Long> {

    List<QuizAttempt>
    findAllByUserIdOrderBySubmittedAtDesc(
            Long userId
    );

    List<QuizAttempt>
    findAllByUserIdAndQuizIdOrderBySubmittedAtDesc(
            Long userId,
            Long quizId
    );
}