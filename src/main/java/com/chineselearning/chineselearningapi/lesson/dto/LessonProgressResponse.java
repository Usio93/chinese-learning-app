package com.chineselearning.chineselearningapi.lesson.dto;

import com.chineselearning.chineselearningapi.lesson.entity.LessonProgressStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonProgressResponse {

    private Long lessonId;

    private String title;

    private Integer hskLevel;

    private LessonProgressStatus status;

    private Double progressPercent;

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    private LocalDateTime lastAccessedAt;
}