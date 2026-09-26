package com.chineselearning.chineselearningapi.progress.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class TodayProgressResponse {

    private LocalDate date;

    private Integer completedTasks;

    private Integer totalTasks;

    private Integer completedMinutes;

    private Integer totalMinutes;

    private Integer progressPercent;

    private Boolean dailyGoalCompleted;

    private Integer currentStreak;

    private Integer longestStreak;
}