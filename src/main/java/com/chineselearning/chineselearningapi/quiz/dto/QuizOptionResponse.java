package com.chineselearning.chineselearningapi.quiz.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizOptionResponse {

    private Long id;

    private String optionText;

    private Integer optionOrder;
}