package com.chineselearning.chineselearningapi.quiz.dto;

import jakarta.validation.constraints.NotNull;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizAnswerRequest {

    @NotNull
    private Long questionId;

    @NotNull
    private Long optionId;
}