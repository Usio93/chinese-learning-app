package com.chineselearning.chineselearningapi.onboarding.service;

import com.chineselearning.chineselearningapi.onboarding.dto.OnboardingRequest;
import com.chineselearning.chineselearningapi.onboarding.dto.OnboardingResponse;
import com.chineselearning.chineselearningapi.user.entity.User;
import com.chineselearning.chineselearningapi.user.entity.UserHskLevel;
import com.chineselearning.chineselearningapi.user.entity.UserProfile;
import com.chineselearning.chineselearningapi.user.repository.UserHskLevelRepository;
import com.chineselearning.chineselearningapi.user.repository.UserProfileRepository;
import com.chineselearning.chineselearningapi.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
@RequiredArgsConstructor
public class OnboardingService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserHskLevelRepository userHskLevelRepository;

    @Transactional
    public OnboardingResponse completeOnboarding(
            String email,
            OnboardingRequest request
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        UserProfile profile =
                userProfileRepository
                        .findByUserId(user.getId())
                        .orElseGet(() ->
                                UserProfile.builder()
                                        .user(user)
                                        .build()
                        );

        profile.setAgeGroup(request.getAgeGroup());
        profile.setOccupation(request.getOccupation());
        profile.setLearningGoal(request.getLearningGoal());

        profile.setPreferredStudyTime(
                request.getPreferredStudyTime()
        );

        profile.setDailyStudyMinutes(
                request.getDailyStudyMinutes()
        );

        profile.setLanguage(request.getLanguage());

        profile.setNotificationEnabled(
                request.getNotificationEnabled()
        );

        userProfileRepository.save(profile);

        updateHskLevels(
                user,
                request.getHskLevels()
        );

        user.setOnboardingCompleted(true);

        userRepository.save(user);

        return OnboardingResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .hskLevels(
                        new TreeSet<>(
                                request.getHskLevels()
                        )
                )
                .ageGroup(profile.getAgeGroup())
                .occupation(profile.getOccupation())
                .learningGoal(profile.getLearningGoal())
                .preferredStudyTime(
                        profile.getPreferredStudyTime()
                )
                .dailyStudyMinutes(
                        profile.getDailyStudyMinutes()
                )
                .language(profile.getLanguage())
                .notificationEnabled(
                        profile.getNotificationEnabled()
                )
                .onboardingCompleted(true)
                .build();
    }

    private void updateHskLevels(
            User user,
            Set<Integer> requestedLevels
    ) {

        Set<Integer> requested =
                new HashSet<>(requestedLevels);

        List<UserHskLevel> existing =
                userHskLevelRepository
                        .findAllByUserId(user.getId());

        /*
         * Những HSK cũ nhưng user không còn chọn
         * sẽ chuyển thành inactive.
         */
        for (UserHskLevel userHsk : existing) {

            userHsk.setActive(
                    requested.contains(
                            userHsk.getHskLevel()
                    )
            );
        }

        userHskLevelRepository.saveAll(existing);

        /*
         * Thêm các HSK mới user vừa chọn.
         */
        for (Integer level : requested) {

            boolean alreadyExists =
                    existing.stream()
                            .anyMatch(hsk ->
                                    hsk.getHskLevel()
                                            .equals(level)
                            );

            if (!alreadyExists) {

                UserHskLevel newLevel =
                        UserHskLevel.builder()
                                .user(user)
                                .hskLevel(level)
                                .active(true)
                                .build();

                userHskLevelRepository.save(newLevel);
            }
        }
    }
}