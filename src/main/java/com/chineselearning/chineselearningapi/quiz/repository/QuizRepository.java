package com.chineselearning.chineselearningapi.quiz.repository;

import com.chineselearning.chineselearningapi.quiz.entity.Quiz;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuizRepository
        extends JpaRepository<Quiz, Long> {

    List<Quiz>
    findAllByLessonIdAndActiveTrue(
            Long lessonId
    );

    Optional<Quiz>
    findByIdAndActiveTrue(
            Long id
    );
}