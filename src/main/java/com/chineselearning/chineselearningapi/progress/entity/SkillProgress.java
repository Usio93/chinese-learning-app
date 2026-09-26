package com.chineselearning.chineselearningapi.progress.entity;

import com.chineselearning.chineselearningapi.study.entity.SkillType;
import com.chineselearning.chineselearningapi.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "skill_progress",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_skill_progress_user_hsk_skill",
                        columnNames = {"user_id", "hsk_level", "skill"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "hsk_level", nullable = false)
    private Integer hskLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "skill", nullable = false, length = 30)
    private SkillType skill;

    @Builder.Default
    @Column(name = "completed_tasks", nullable = false)
    private Integer completedTasks = 0;

    @Builder.Default
    @Column(name = "completed_minutes", nullable = false)
    private Integer completedMinutes = 0;

    // Sau này Quiz/Exercise cập nhật
    @Builder.Default
    @Column(name = "exercise_attempts", nullable = false)
    private Integer exerciseAttempts = 0;

    @Builder.Default
    @Column(name = "correct_answers", nullable = false)
    private Integer correctAnswers = 0;

    // Sau này AI Writing / Pronunciation cập nhật
    @Column(name = "average_score")
    private Double averageScore;
}