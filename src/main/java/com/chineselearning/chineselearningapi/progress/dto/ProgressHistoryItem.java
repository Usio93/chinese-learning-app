package com.chineselearning.chineselearningapi.progress.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ProgressHistoryItem {

    private LocalDate date;

    private Integer completedMinutes;

    private Integer totalMinutes;

    private Integer progressPercent;

    private Boolean goalCompleted;
}