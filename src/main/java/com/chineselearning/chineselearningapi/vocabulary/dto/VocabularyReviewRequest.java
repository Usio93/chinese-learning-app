package com.chineselearning.chineselearningapi.vocabulary.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VocabularyReviewRequest {

    @NotNull
    private Boolean correct;
}