package com.chineselearning.chineselearningapi.vocabulary.dto;

import com.chineselearning.chineselearningapi.vocabulary.entity.VocabularyStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VocabularyResponse {

    private Long id;

    private String hanzi;

    private String pinyin;

    private String meaning;

    private String exampleSentence;

    private String examplePinyin;

    private String exampleMeaning;

    private String audioUrl;

    private Integer hskLevel;

    private VocabularyStatus status;

    private Integer correctCount;

    private Integer wrongCount;

    private Integer reviewCount;
}