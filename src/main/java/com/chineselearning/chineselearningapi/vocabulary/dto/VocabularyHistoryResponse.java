package com.chineselearning.chineselearningapi.vocabulary.dto;

import com.chineselearning.chineselearningapi.vocabulary.entity.VocabularyStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VocabularyHistoryResponse {

    private Long vocabularyId;

    private String hanzi;

    private String pinyin;

    private String meaning;

    private Integer hskLevel;

    private VocabularyStatus status;

    private Integer correctCount;

    private Integer wrongCount;

    private Integer reviewCount;

    private LocalDateTime firstLearnedAt;

    private LocalDateTime lastReviewedAt;

    private LocalDateTime nextReviewAt;
}