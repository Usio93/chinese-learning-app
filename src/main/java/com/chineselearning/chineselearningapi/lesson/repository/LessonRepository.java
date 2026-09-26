package com.chineselearning.chineselearningapi.lesson.repository;

import com.chineselearning.chineselearningapi.lesson.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository
        extends JpaRepository<Lesson, Long> {

    List<Lesson>
    findAllByHskLevelAndActiveTrueOrderByLessonOrderAsc(
            Integer hskLevel
    );

    List<Lesson>
    findAllByHskLevelInAndActiveTrueOrderByHskLevelAscLessonOrderAsc(
            List<Integer> hskLevels
    );
}