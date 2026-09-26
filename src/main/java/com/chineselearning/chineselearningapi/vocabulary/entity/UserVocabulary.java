package com.chineselearning.chineselearningapi.vocabulary.entity;

import com.chineselearning.chineselearningapi.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_vocabularies",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_vocabulary",
                        columnNames = {
                                "user_id",
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
public class UserVocabulary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "vocabulary_id",
            nullable = false
    )
    private Vocabulary vocabulary;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    @Builder.Default
    private VocabularyStatus status =
            VocabularyStatus.NEW;

    @Builder.Default
    @Column(
            name = "correct_count",
            nullable = false
    )
    private Integer correctCount = 0;

    @Builder.Default
    @Column(
            name = "wrong_count",
            nullable = false
    )
    private Integer wrongCount = 0;

    @Builder.Default
    @Column(
            name = "review_count",
            nullable = false
    )
    private Integer reviewCount = 0;

    @Column(
            name = "last_reviewed_at"
    )
    private LocalDateTime lastReviewedAt;

    @Column(
            name = "next_review_at"
    )
    private LocalDateTime nextReviewAt;

    @Column(
            name = "first_learned_at"
    )
    private LocalDateTime firstLearnedAt;
}