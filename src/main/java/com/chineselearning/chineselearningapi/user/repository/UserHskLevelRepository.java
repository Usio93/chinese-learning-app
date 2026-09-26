package com.chineselearning.chineselearningapi.user.repository;

import com.chineselearning.chineselearningapi.user.entity.UserHskLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserHskLevelRepository
        extends JpaRepository<UserHskLevel, Long> {

    List<UserHskLevel> findAllByUserId(Long userId);

    List<UserHskLevel> findAllByUserIdAndActiveTrue(Long userId);

    Optional<UserHskLevel> findByUserIdAndHskLevel(
            Long userId,
            Integer hskLevel
    );
}