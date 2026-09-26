package com.chineselearning.chineselearningapi.vocabulary.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VocabularyDashboardResponse {

    // ===== TODAY =====
    private Integer todayTarget;
    private Integer todayCompleted;
    private Integer todayRemaining;
    private Integer todayNewWords;
    private Integer todayReviewWords;

    // ===== ALL TIME =====
    private Long totalLearned;
    private Long learning;
    private Long familiar;
    private Long mastered;

    // ===== REVIEW =====
    private Long dueForReview;

    // ===== PROGRESS =====
    private Double todayProgressPercent;
}