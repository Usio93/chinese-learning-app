package com.chineselearning.chineselearningapi.onboarding.dto;

import com.chineselearning.chineselearningapi.user.entity.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class OnboardingRequest {

    @NotEmpty
    @Size(min = 1, max = 2)
    private Set<
            @Min(1)
            @Max(6)
                    Integer
            > hskLevels;

    @NotNull
    private AgeGroup ageGroup;

    @NotNull
    private Occupation occupation;

    @NotNull
    private LearningGoal learningGoal;

    @NotNull
    private StudyTime preferredStudyTime;

    @NotNull
    @Min(10)
    @Max(240)
    private Integer dailyStudyMinutes;

    @NotNull
    private AppLanguage language;

    @NotNull
    private Boolean notificationEnabled;
}