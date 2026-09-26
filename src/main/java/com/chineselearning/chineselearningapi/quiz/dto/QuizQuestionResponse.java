package com.chineselearning.chineselearningapi.quiz.dto;

import com.chineselearning.chineselearningapi.quiz.entity.QuestionType;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizQuestionResponse {

    private Long id;

    private QuestionType questionType;

    private String questionText;

    private String questionPinyin;

    private Integer questionOrder;

    private Integer points;

    private List<QuizOptionResponse> options;
}