package com.chineselearning.chineselearningapi.vocabulary.entity;

import com.chineselearning.chineselearningapi.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "daily_vocabulary_sets",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_daily_vocab_user_date",
                        columnNames = {"user_id", "study_date"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyVocabularySet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @Column(
            name = "study_date",
            nullable = false
    )
    private LocalDate studyDate;

    @Builder.Default
    @Column(
            name = "target_words",
            nullable = false
    )
    private Integer targetWords = 20;

    @Builder.Default
    @Column(
            name = "completed_words",
            nullable = false
    )
    private Integer completedWords = 0;

    @OneToMany(
            mappedBy = "dailySet",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<DailyVocabularyItem> items =
            new ArrayList<>();
}