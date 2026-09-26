package com.chineselearning.chineselearningapi.study.repository;

import com.chineselearning.chineselearningapi.study.entity.DailyTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyTaskRepository
        extends JpaRepository<DailyTask, Long> {
}