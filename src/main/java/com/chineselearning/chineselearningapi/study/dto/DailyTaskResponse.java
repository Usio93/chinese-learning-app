package com.chineselearning.chineselearningapi.study.dto;

import com.chineselearning.chineselearningapi.study.entity.SkillType;
import com.chineselearning.chineselearningapi.study.entity.TaskStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DailyTaskResponse {

    private Long id;

    private Integer hskLevel;

    private SkillType skill;

    private Integer minutes;

    private TaskStatus status;
}