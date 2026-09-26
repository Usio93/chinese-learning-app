package com.chineselearning.chineselearningapi.quiz.entity;

import com.chineselearning.chineselearningapi.user.entity.User;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "quiz_attempts",
        indexes = {
                @Index(
                        name = "idx_attempt_user",
                        columnList = "user_id"
                ),

                @Index(
                        name = "idx_attempt_quiz",
                        columnList = "quiz_id"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizAttempt {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "quiz_id",
            nullable = false
    )
    private Quiz quiz;

    @Column(
            name = "total_questions",
            nullable = false
    )
    private Integer totalQuestions;

    @Column(
            name = "correct_answers",
            nullable = false
    )
    private Integer correctAnswers;

    @Column(
            nullable = false
    )
    private Double score;

    @Column(
            nullable = false
    )
    private Boolean passed;

    @Column(
            name = "submitted_at",
            nullable = false
    )
    private LocalDateTime submittedAt;
}