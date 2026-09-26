package com.chineselearning.chineselearningapi.quiz.entity;

import com.chineselearning.chineselearningapi.lesson.entity.Lesson;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "quizzes",
        indexes = {
                @Index(
                        name = "idx_quiz_lesson",
                        columnList = "lesson_id"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {

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
            name = "lesson_id",
            nullable = false
    )
    private Lesson lesson;

    @Column(
            nullable = false,
            length = 200
    )
    private String title;

    @Column(
            columnDefinition = "TEXT"
    )
    private String description;

    @Column(
            name = "pass_score",
            nullable = false
    )
    private Double passScore;

    @Column(
            nullable = false
    )
    private Boolean active;

    @Column(
            name = "created_at",
            nullable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = "updated_at"
    )
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {

        if (passScore == null) {
            passScore = 70.0;
        }

        if (active == null) {
            active = true;
        }

        createdAt =
                LocalDateTime.now();

        updatedAt =
                LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {

        updatedAt =
                LocalDateTime.now();
    }
}