package com.chineselearning.chineselearningapi.lesson.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "lessons",
        indexes = {
                @Index(
                        name = "idx_lesson_hsk",
                        columnList = "hsk_level"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

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
            name = "hsk_level",
            nullable = false
    )
    private Integer hskLevel;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 30
    )
    private LessonSkill skill;

    @Column(
            name = "lesson_order",
            nullable = false
    )
    private Integer lessonOrder;

    @Column(
            name = "estimated_minutes"
    )
    private Integer estimatedMinutes;

    @Column(
            name = "thumbnail_url"
    )
    private String thumbnailUrl;

    @Column(
            nullable = false
    )
    private Boolean active;

    @Column(
            name = "premium_only",
            nullable = false
    )
    private Boolean premiumOnly;

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

        LocalDateTime now =
                LocalDateTime.now();

        if (active == null) {
            active = true;
        }

        if (premiumOnly == null) {
            premiumOnly = false;
        }

        if (lessonOrder == null) {
            lessonOrder = 0;
        }

        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {

        updatedAt =
                LocalDateTime.now();
    }
}