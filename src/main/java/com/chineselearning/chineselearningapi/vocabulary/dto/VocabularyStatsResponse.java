package com.chineselearning.chineselearningapi.vocabulary.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VocabularyStatsResponse {

    private Long total;

    private Long newWords;

    private Long learning;

    private Long familiar;

    private Long mastered;
}