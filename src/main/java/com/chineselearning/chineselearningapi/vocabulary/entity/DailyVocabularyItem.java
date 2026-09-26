package com.chineselearning.chineselearningapi.vocabulary.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "daily_vocabulary_items",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_daily_set_vocabulary",
                        columnNames = {
                                "daily_set_id",
                                "vocabulary_id"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyVocabularyItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "daily_set_id",
            nullable = false
    )
    private DailyVocabularySet dailySet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "vocabulary_id",
            nullable = false
    )
    private Vocabulary vocabulary;

    @Builder.Default
    @Column(
            name = "is_review",
            nullable = false
    )
    private Boolean review = false;

    @Builder.Default
    @Column(
            name = "is_completed",
            nullable = false
    )
    private Boolean completed = false;
}