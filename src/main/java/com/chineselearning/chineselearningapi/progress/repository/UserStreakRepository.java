package com.chineselearning.chineselearningapi.progress.repository;

import com.chineselearning.chineselearningapi.progress.entity.UserStreak;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserStreakRepository
        extends JpaRepository<UserStreak, Long> {

    Optional<UserStreak> findByUserId(Long userId);
}