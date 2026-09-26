package com.chineselearning.chineselearningapi.onboarding.dto;

import com.chineselearning.chineselearningapi.user.entity.*;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class OnboardingResponse {

    private Long userId;
    private String email;

    private Set<Integer> hskLevels;

    private AgeGroup ageGroup;
    private Occupation occupation;
    private LearningGoal learningGoal;
    private StudyTime preferredStudyTime;
    private Integer dailyStudyMinutes;
    private AppLanguage language;
    private Boolean notificationEnabled;

    private Boolean onboardingCompleted;
}