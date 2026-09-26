package com.chineselearning.chineselearningapi.study.entity;

import com.chineselearning.chineselearningapi.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "study_plans",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_study_plan_user_date",
                        columnNames = {
                                "user_id",
                                "plan_date"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyPlan {

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
            name = "plan_date",
            nullable = false
    )
    private LocalDate planDate;

    @Column(
            name = "total_minutes",
            nullable = false
    )
    private Integer totalMinutes;

    @Column(
            name = "completed_minutes",
            nullable = false
    )
    @Builder.Default
    private Integer completedMinutes = 0;

    @OneToMany(
            mappedBy = "studyPlan",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<DailyTask> tasks =
            new ArrayList<>();
}