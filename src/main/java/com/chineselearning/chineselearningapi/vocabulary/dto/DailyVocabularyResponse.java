package com.chineselearning.chineselearningapi.vocabulary.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class DailyVocabularyResponse {

    private LocalDate date;

    private Integer targetWords;

    private Integer completedWords;

    private Integer remainingWords;

    private Integer reviewWords;

    private Integer newWords;

    private List<DailyVocabularyItemResponse> items;
}