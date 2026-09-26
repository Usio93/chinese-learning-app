package com.chineselearning.chineselearningapi.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true
    )
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AppLanguage language;

    @Enumerated(EnumType.STRING)
    @Column(name = "age_group", length = 30)
    private AgeGroup ageGroup;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private Occupation occupation;


    @Enumerated(EnumType.STRING)
    @Column(name = "learning_goal", length = 50)
    private LearningGoal learningGoal;

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_study_time", length = 30)
    private StudyTime preferredStudyTime;

    @Column(name = "daily_study_minutes")
    private Integer dailyStudyMinutes;

    @Builder.Default
    @Column(name = "notification_enabled", nullable = false)
    private Boolean notificationEnabled = true;
}