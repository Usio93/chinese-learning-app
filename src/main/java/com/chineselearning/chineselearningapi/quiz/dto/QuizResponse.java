package com.chineselearning.chineselearningapi.quiz.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizResponse {

    private Long id;

    private Long lessonId;

    private String title;

    private String description;

    private Double passScore;

    private Integer totalQuestions;

    private List<QuizQuestionResponse> questions;
}