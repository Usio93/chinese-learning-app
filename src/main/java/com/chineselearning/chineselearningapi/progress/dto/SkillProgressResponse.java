package com.chineselearning.chineselearningapi.progress.dto;

import com.chineselearning.chineselearningapi.study.entity.SkillType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SkillProgressResponse {

    private Integer hskLevel;

    private SkillType skill;

    private Integer completedTasks;

    private Integer completedMinutes;

    private Integer exerciseAttempts;

    private Integer correctAnswers;

    private Double accuracy;

    private Double averageScore;
}