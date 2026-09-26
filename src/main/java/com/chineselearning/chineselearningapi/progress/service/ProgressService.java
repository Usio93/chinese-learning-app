package com.chineselearning.chineselearningapi.progress.service;

import com.chineselearning.chineselearningapi.progress.dto.*;
import com.chineselearning.chineselearningapi.progress.entity.DailyProgress;
import com.chineselearning.chineselearningapi.progress.entity.UserStreak;
import com.chineselearning.chineselearningapi.progress.repository.DailyProgressRepository;
import com.chineselearning.chineselearningapi.progress.repository.UserStreakRepository;
import com.chineselearning.chineselearningapi.study.entity.StudyPlan;
import com.chineselearning.chineselearningapi.study.entity.TaskStatus;
import com.chineselearning.chineselearningapi.study.repository.StudyPlanRepository;
import com.chineselearning.chineselearningapi.user.entity.User;
import com.chineselearning.chineselearningapi.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final UserRepository userRepository;
    private final StudyPlanRepository studyPlanRepository;

    private final DailyProgressRepository
            dailyProgressRepository;

    private final UserStreakRepository
            userStreakRepository;

    @Transactional
    public void syncTodayProgress(
            User user,
            StudyPlan plan
    ) {

        LocalDate today =
                plan.getPlanDate();

        DailyProgress progress =
                dailyProgressRepository
                        .findByUserIdAndProgressDate(
                                user.getId(),
                                today
                        )
                        .orElseGet(() ->
                                DailyProgress.builder()
                                        .user(user)
                                        .progressDate(today)
                                        .build()
                        );

        int completedTasks =
                (int) plan.getTasks()
                        .stream()
                        .filter(task ->
                                task.getStatus()
                                        == TaskStatus.COMPLETED
                        )
                        .count();

        int totalTasks =
                plan.getTasks().size();

        int completedMinutes =
                plan.getCompletedMinutes();

        int totalMinutes =
                plan.getTotalMinutes();

        int percentage = 0;

        if (totalMinutes > 0) {

            percentage =
                    (int) Math.round(
                            completedMinutes
                                    * 100.0
                                    / totalMinutes
                    );
        }

        boolean wasCompleted =
                Boolean.TRUE.equals(
                        progress.getDailyGoalCompleted()
                );

        boolean goalCompleted =
                percentage >= 100;

        progress.setCompletedTasks(completedTasks);
        progress.setTotalTasks(totalTasks);
        progress.setCompletedMinutes(completedMinutes);
        progress.setTotalMinutes(totalMinutes);
        progress.setProgressPercent(percentage);
        progress.setDailyGoalCompleted(goalCompleted);

        dailyProgressRepository.save(progress);

        /*
         * Chỉ update streak vào thời điểm
         * user vừa hoàn thành mục tiêu ngày.
         *
         * Tránh complete lại API khiến streak tăng nhiều lần.
         */
        if (!wasCompleted && goalCompleted) {
            updateStreak(user, today);
        }
    }

    private void updateStreak(
            User user,
            LocalDate completedDate
    ) {

        UserStreak streak =
                userStreakRepository
                        .findByUserId(user.getId())
                        .orElseGet(() ->
                                UserStreak.builder()
                                        .user(user)
                                        .build()
                        );

        LocalDate lastDate =
                streak.getLastCompletedDate();

        if (lastDate == null) {

            streak.setCurrentStreak(1);

        } else if (
                lastDate.equals(
                        completedDate.minusDays(1)
                )
        ) {

            streak.setCurrentStreak(
                    streak.getCurrentStreak() + 1
            );

        } else if (!lastDate.equals(completedDate)) {

            streak.setCurrentStreak(1);
        }

        if (streak.getCurrentStreak()
                > streak.getLongestStreak()) {

            streak.setLongestStreak(
                    streak.getCurrentStreak()
            );
        }

        streak.setLastCompletedDate(completedDate);

        userStreakRepository.save(streak);
    }

    @Transactional(readOnly = true)
    public TodayProgressResponse getTodayProgress(
            String email
    ) {

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );

        LocalDate today =
                LocalDate.now();

        DailyProgress progress =
                dailyProgressRepository
                        .findByUserIdAndProgressDate(
                                user.getId(),
                                today
                        )
                        .orElse(
                                DailyProgress.builder()
                                        .user(user)
                                        .progressDate(today)
                                        .completedTasks(0)
                                        .totalTasks(0)
                                        .completedMinutes(0)
                                        .totalMinutes(0)
                                        .progressPercent(0)
                                        .dailyGoalCompleted(false)
                                        .build()
                        );

        UserStreak streak =
                userStreakRepository
                        .findByUserId(user.getId())
                        .orElse(
                                UserStreak.builder()
                                        .user(user)
                                        .currentStreak(0)
                                        .longestStreak(0)
                                        .build()
                        );

        return TodayProgressResponse.builder()
                .date(today)
                .completedTasks(
                        progress.getCompletedTasks()
                )
                .totalTasks(
                        progress.getTotalTasks()
                )
                .completedMinutes(
                        progress.getCompletedMinutes()
                )
                .totalMinutes(
                        progress.getTotalMinutes()
                )
                .progressPercent(
                        progress.getProgressPercent()
                )
                .dailyGoalCompleted(
                        progress.getDailyGoalCompleted()
                )
                .currentStreak(
                        streak.getCurrentStreak()
                )
                .longestStreak(
                        streak.getLongestStreak()
                )
                .build();
    }

    @Transactional(readOnly = true)
    public ProgressHistoryResponse getHistory(
            String email,
            int days
    ) {

        if (days < 1 || days > 90) {
            throw new RuntimeException(
                    "Days must be between 1 and 90"
            );
        }

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );

        LocalDate end =
                LocalDate.now();

        LocalDate start =
                end.minusDays(days - 1L);

        List<ProgressHistoryItem> history =
                dailyProgressRepository
                        .findAllByUserIdAndProgressDateBetweenOrderByProgressDateAsc(
                                user.getId(),
                                start,
                                end
                        )
                        .stream()
                        .map(progress ->
                                ProgressHistoryItem.builder()
                                        .date(
                                                progress.getProgressDate()
                                        )
                                        .completedMinutes(
                                                progress.getCompletedMinutes()
                                        )
                                        .totalMinutes(
                                                progress.getTotalMinutes()
                                        )
                                        .progressPercent(
                                                progress.getProgressPercent()
                                        )
                                        .goalCompleted(
                                                progress.getDailyGoalCompleted()
                                        )
                                        .build()
                        )
                        .toList();

        return ProgressHistoryResponse.builder()
                .days(days)
                .history(history)
                .build();
    }
}