package com.chineselearning.chineselearningapi.study.repository;

import com.chineselearning.chineselearningapi.study.entity.StudyPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface StudyPlanRepository
        extends JpaRepository<StudyPlan, Long> {

    Optional<StudyPlan> findByUserIdAndPlanDate(
            Long userId,
            LocalDate planDate
    );
}