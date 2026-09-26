package com.chineselearning.chineselearningapi.study.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Builder
public class StudyPlanResponse {

    private Long planId;

    private LocalDate date;

    private Set<Integer> hskLevels;

    private Integer totalMinutes;

    private Integer completedMinutes;

    private Integer progressPercent;

    private List<DailyTaskResponse> tasks;
}