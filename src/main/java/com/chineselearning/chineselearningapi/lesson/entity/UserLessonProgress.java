package com.chineselearning.chineselearningapi.lesson.entity;

import com.chineselearning.chineselearningapi.user.entity.User;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_lesson_progress",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_lesson_progress",
                        columnNames = {
                                "user_id",
                                "lesson_id"
                        }
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLessonProgress {

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
            name = "lesson_id",
            nullable = false
    )
    private Lesson lesson;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 30
    )
    private LessonProgressStatus status;

    @Column(
            name = "progress_percent",
            nullable = false
    )
    private Double progressPercent;

    @Column(
            name = "started_at"
    )
    private LocalDateTime startedAt;

    @Column(
            name = "completed_at"
    )
    private LocalDateTime completedAt;

    @Column(
            name = "last_accessed_at"
    )
    private LocalDateTime lastAccessedAt;

    @PrePersist
    public void prePersist() {

        if (status == null) {
            status =
                    LessonProgressStatus.NOT_STARTED;
        }

        if (progressPercent == null) {
            progressPercent = 0.0;
        }
    }
}