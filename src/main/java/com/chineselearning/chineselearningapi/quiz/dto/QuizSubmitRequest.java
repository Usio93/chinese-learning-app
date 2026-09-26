package com.chineselearning.chineselearningapi.quiz.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizSubmitRequest {

    @NotEmpty
    @Valid
    private List<QuizAnswerRequest> answers;
}