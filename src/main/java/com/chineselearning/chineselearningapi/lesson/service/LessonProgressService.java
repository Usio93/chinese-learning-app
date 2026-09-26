package com.chineselearning.chineselearningapi.lesson.service;

import com.chineselearning.chineselearningapi.lesson.dto.LessonProgressResponse;

import com.chineselearning.chineselearningapi.lesson.entity.Lesson;
import com.chineselearning.chineselearningapi.lesson.entity.LessonProgressStatus;
import com.chineselearning.chineselearningapi.lesson.entity.UserLessonProgress;

import com.chineselearning.chineselearningapi.lesson.repository.LessonRepository;
import com.chineselearning.chineselearningapi.lesson.repository.UserLessonProgressRepository;

import com.chineselearning.chineselearningapi.user.entity.User;
import com.chineselearning.chineselearningapi.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonProgressService {

    private final UserRepository userRepository;

    private final LessonRepository lessonRepository;

    private final UserLessonProgressRepository
            userLessonProgressRepository;

    // =========================================================
    // START LESSON
    // =========================================================

    @Transactional
    public LessonProgressResponse startLesson(
            String email,
            Long lessonId
    ) {

        User user =
                getUser(email);

        Lesson lesson =
                getLesson(lessonId);

        UserLessonProgress progress =
                userLessonProgressRepository
                        .findByUserIdAndLessonId(
                                user.getId(),
                                lessonId
                        )
                        .orElseGet(() ->
                                UserLessonProgress
                                        .builder()
                                        .user(user)
                                        .lesson(lesson)
                                        .status(
                                                LessonProgressStatus.NOT_STARTED
                                        )
                                        .progressPercent(0.0)
                                        .build()
                        );

        LocalDateTime now =
                LocalDateTime.now();

        if (progress.getStartedAt() == null) {
            progress.setStartedAt(now);
        }

        if (progress.getStatus()
                != LessonProgressStatus.COMPLETED) {

            progress.setStatus(
                    LessonProgressStatus.IN_PROGRESS
            );
        }

        progress.setLastAccessedAt(now);

        userLessonProgressRepository
                .save(progress);

        return toResponse(progress);
    }

    // =========================================================
    // COMPLETE LESSON
    // =========================================================

    @Transactional
    public LessonProgressResponse completeLesson(
            String email,
            Long lessonId
    ) {

        User user =
                getUser(email);

        Lesson lesson =
                getLesson(lessonId);

        UserLessonProgress progress =
                userLessonProgressRepository
                        .findByUserIdAndLessonId(
                                user.getId(),
                                lessonId
                        )
                        .orElseGet(() ->
                                UserLessonProgress
                                        .builder()
                                        .user(user)
                                        .lesson(lesson)
                                        .build()
                        );

        LocalDateTime now =
                LocalDateTime.now();

        if (progress.getStartedAt() == null) {
            progress.setStartedAt(now);
        }

        progress.setStatus(
                LessonProgressStatus.COMPLETED
        );

        progress.setProgressPercent(
                100.0
        );

        progress.setCompletedAt(now);

        progress.setLastAccessedAt(now);

        userLessonProgressRepository
                .save(progress);

        return toResponse(progress);
    }

    // =========================================================
    // MY PROGRESS
    // =========================================================

    @Transactional(readOnly = true)
    public List<LessonProgressResponse> getMyProgress(
            String email
    ) {

        User user =
                getUser(email);

        return userLessonProgressRepository
                .findAllByUserId(
                        user.getId()
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private User getUser(
            String email
    ) {

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );
    }

    private Lesson getLesson(
            Long lessonId
    ) {

        return lessonRepository
                .findById(lessonId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Lesson not found"
                        )
                );
    }

    private LessonProgressResponse toResponse(
            UserLessonProgress progress
    ) {

        Lesson lesson =
                progress.getLesson();

        return LessonProgressResponse
                .builder()

                .lessonId(
                        lesson.getId()
                )

                .title(
                        lesson.getTitle()
                )

                .hskLevel(
                        lesson.getHskLevel()
                )

                .status(
                        progress.getStatus()
                )

                .progressPercent(
                        progress.getProgressPercent()
                )

                .startedAt(
                        progress.getStartedAt()
                )

                .completedAt(
                        progress.getCompletedAt()
                )

                .lastAccessedAt(
                        progress.getLastAccessedAt()
                )

                .build();
    }
}