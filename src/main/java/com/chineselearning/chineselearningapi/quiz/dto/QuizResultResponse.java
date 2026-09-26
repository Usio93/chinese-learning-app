package com.chineselearning.chineselearningapi.quiz.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizResultResponse {

    private Long attemptId;

    private Long quizId;

    private String quizTitle;

    private Integer totalQuestions;

    private Integer answeredQuestions;

    private Integer correctAnswers;

    private Integer wrongAnswers;

    private Double score;

    private Double passScore;

    private Boolean passed;

    private LocalDateTime submittedAt;
}