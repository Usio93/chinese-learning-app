package com.chineselearning.chineselearningapi.progress.repository;

import com.chineselearning.chineselearningapi.progress.entity.DailyProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyProgressRepository
        extends JpaRepository<DailyProgress, Long> {

    Optional<DailyProgress>
    findByUserIdAndProgressDate(
            Long userId,
            LocalDate progressDate
    );

    List<DailyProgress>
    findAllByUserIdAndProgressDateBetweenOrderByProgressDateAsc(
            Long userId,
            LocalDate startDate,
            LocalDate endDate
    );
}