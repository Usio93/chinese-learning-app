package com.chineselearning.chineselearningapi.lesson.dto;

import com.chineselearning.chineselearningapi.lesson.entity.LessonContentType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonContentResponse {

    private Long id;

    private LessonContentType contentType;

    private String title;

    private String textContent;

    private String pinyinContent;

    private String translationVi;

    private String audioUrl;

    private String videoUrl;

    private Integer contentOrder;
}