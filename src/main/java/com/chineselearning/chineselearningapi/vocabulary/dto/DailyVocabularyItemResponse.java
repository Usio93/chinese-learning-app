package com.chineselearning.chineselearningapi.vocabulary.dto;

import com.chineselearning.chineselearningapi.vocabulary.entity.VocabularyStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DailyVocabularyItemResponse {

    private Long itemId;

    private Long vocabularyId;

    private Integer hskLevel;

    private String hanzi;

    private String pinyin;

    private String meaning;

    private Boolean review;

    private Boolean completed;

    private VocabularyStatus status;
}