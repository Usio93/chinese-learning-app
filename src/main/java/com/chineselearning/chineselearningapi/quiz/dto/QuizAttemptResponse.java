package com.chineselearning.chineselearningapi.quiz.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizAttemptResponse {

    private Long attemptId;

    private Long quizId;

    private String quizTitle;

    private Long lessonId;

    private Integer hskLevel;

    private Double score;

    private Boolean passed;

    private Integer correctAnswers;

    private Integer totalQuestions;

    private LocalDateTime submittedAt;
}