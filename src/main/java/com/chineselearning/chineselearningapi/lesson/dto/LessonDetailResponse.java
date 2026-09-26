package com.chineselearning.chineselearningapi.lesson.dto;

import com.chineselearning.chineselearningapi.lesson.entity.LessonSkill;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonDetailResponse {

    private Long id;

    private String title;

    private String description;

    private Integer hskLevel;

    private LessonSkill skill;

    private Integer estimatedMinutes;

    private Boolean premiumOnly;

    private List<LessonContentResponse> contents;
}