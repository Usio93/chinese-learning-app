package com.chineselearning.chineselearningapi.lesson.service;

import com.chineselearning.chineselearningapi.lesson.dto.LessonResponse;
import com.chineselearning.chineselearningapi.lesson.entity.Lesson;
import com.chineselearning.chineselearningapi.lesson.repository.LessonRepository;

import com.chineselearning.chineselearningapi.user.entity.User;
import com.chineselearning.chineselearningapi.user.entity.UserHskLevel;
import com.chineselearning.chineselearningapi.user.repository.UserHskLevelRepository;
import com.chineselearning.chineselearningapi.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;

    private final UserRepository userRepository;

    private final UserHskLevelRepository
            userHskLevelRepository;

    // =========================================================
    // LESSONS BY ONE HSK
    // =========================================================

    @Transactional(readOnly = true)
    public List<LessonResponse> getByHsk(
            Integer hskLevel
    ) {

        if (hskLevel < 1 ||
                hskLevel > 6) {

            throw new RuntimeException(
                    "HSK level must be between 1 and 6"
            );
        }

        return lessonRepository
                .findAllByHskLevelAndActiveTrueOrderByLessonOrderAsc(
                        hskLevel
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================================================
    // LESSONS FOR CURRENT USER
    // =========================================================

    @Transactional(readOnly = true)
    public List<LessonResponse> getMyLessons(
            String email
    ) {

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );

        List<Integer> activeHskLevels =
                userHskLevelRepository
                        .findAllByUserIdAndActiveTrue(
                                user.getId()
                        )
                        .stream()
                        .map(
                                UserHskLevel::getHskLevel
                        )
                        .toList();

        if (activeHskLevels.isEmpty()) {

            return List.of();
        }

        return lessonRepository
                .findAllByHskLevelInAndActiveTrueOrderByHskLevelAscLessonOrderAsc(
                        activeHskLevels
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================================================
    // LESSON DETAIL
    // =========================================================

    @Transactional(readOnly = true)
    public LessonResponse getById(
            Long lessonId
    ) {

        Lesson lesson =
                lessonRepository
                        .findById(lessonId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Lesson not found"
                                )
                        );

        if (!Boolean.TRUE.equals(
                lesson.getActive()
        )) {

            throw new RuntimeException(
                    "Lesson is not available"
            );
        }

        return toResponse(lesson);
    }

    private LessonResponse toResponse(
            Lesson lesson
    ) {

        return LessonResponse.builder()

                .id(
                        lesson.getId()
                )

                .title(
                        lesson.getTitle()
                )

                .description(
                        lesson.getDescription()
                )

                .hskLevel(
                        lesson.getHskLevel()
                )

                .skill(
                        lesson.getSkill()
                )

                .lessonOrder(
                        lesson.getLessonOrder()
                )

                .estimatedMinutes(
                        lesson.getEstimatedMinutes()
                )

                .thumbnailUrl(
                        lesson.getThumbnailUrl()
                )

                .premiumOnly(
                        lesson.getPremiumOnly()
                )

                .build();
    }
}