package com.chineselearning.chineselearningapi.lesson.repository;

import com.chineselearning.chineselearningapi.lesson.entity.UserLessonProgress;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserLessonProgressRepository
        extends JpaRepository<
        UserLessonProgress,
        Long
        > {

    Optional<UserLessonProgress>
    findByUserIdAndLessonId(
            Long userId,
            Long lessonId
    );

    List<UserLessonProgress>
    findAllByUserId(
            Long userId
    );
}