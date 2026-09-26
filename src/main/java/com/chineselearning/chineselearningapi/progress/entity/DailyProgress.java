package com.chineselearning.chineselearningapi.progress.entity;

import com.chineselearning.chineselearningapi.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "daily_progress",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_daily_progress_user_date",
                        columnNames = {"user_id", "progress_date"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyProgress {

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
            name = "progress_date",
            nullable = false
    )
    private LocalDate progressDate;

    @Builder.Default
    @Column(
            name = "completed_tasks",
            nullable = false
    )
    private Integer completedTasks = 0;

    @Builder.Default
    @Column(
            name = "total_tasks",
            nullable = false
    )
    private Integer totalTasks = 0;

    @Builder.Default
    @Column(
            name = "completed_minutes",
            nullable = false
    )
    private Integer completedMinutes = 0;

    @Builder.Default
    @Column(
            name = "total_minutes",
            nullable = false
    )
    private Integer totalMinutes = 0;

    @Builder.Default
    @Column(
            name = "progress_percent",
            nullable = false
    )
    private Integer progressPercent = 0;

    @Builder.Default
    @Column(
            name = "daily_goal_completed",
            nullable = false
    )
    private Boolean dailyGoalCompleted = false;
}