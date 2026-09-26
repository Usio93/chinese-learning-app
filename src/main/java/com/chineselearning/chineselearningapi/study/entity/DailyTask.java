package com.chineselearning.chineselearningapi.study.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "daily_tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "study_plan_id",
            nullable = false
    )
    private StudyPlan studyPlan;

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
    private SkillType skill;

    @Column(nullable = false)
    private Integer minutes;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 20
    )
    @Builder.Default
    private TaskStatus status =
            TaskStatus.PENDING;
}