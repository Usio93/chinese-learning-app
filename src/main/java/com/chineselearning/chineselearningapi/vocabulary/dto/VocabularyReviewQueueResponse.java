package com.chineselearning.chineselearningapi.vocabulary.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VocabularyReviewQueueResponse {

    private Integer dueCount;

    private Integer returnedCount;

    private List<VocabularyReviewQueueItemResponse> items;
}