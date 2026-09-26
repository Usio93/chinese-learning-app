package com.chineselearning.chineselearningapi.quiz.repository;

import com.chineselearning.chineselearningapi.quiz.entity.QuizOption;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizOptionRepository
        extends JpaRepository<QuizOption, Long> {

    List<QuizOption>
    findAllByQuestionIdOrderByOptionOrderAsc(
            Long questionId
    );
}