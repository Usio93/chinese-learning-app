package com.chineselearning.chineselearningapi.lesson.repository;

import com.chineselearning.chineselearningapi.lesson.entity.LessonContent;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonContentRepository
        extends JpaRepository<LessonContent, Long> {

    List<LessonContent>
    findAllByLessonIdOrderByContentOrderAsc(
            Long lessonId
    );
}