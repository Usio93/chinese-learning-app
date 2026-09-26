package com.chineselearning.chineselearningapi.lesson.dto;

import com.chineselearning.chineselearningapi.lesson.entity.LessonSkill;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonResponse {

    private Long id;

    private String title;

    private String description;

    private Integer hskLevel;

    private LessonSkill skill;

    private Integer lessonOrder;

    private Integer estimatedMinutes;

    private String thumbnailUrl;

    private Boolean premiumOnly;
}