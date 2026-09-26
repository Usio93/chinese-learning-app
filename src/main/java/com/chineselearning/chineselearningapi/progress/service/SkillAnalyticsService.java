package com.chineselearning.chineselearningapi.progress.service;

import com.chineselearning.chineselearningapi.progress.dto.SkillAnalyticsResponse;
import com.chineselearning.chineselearningapi.progress.dto.SkillProgressResponse;
import com.chineselearning.chineselearningapi.progress.entity.SkillProgress;
import com.chineselearning.chineselearningapi.progress.repository.SkillProgressRepository;
import com.chineselearning.chineselearningapi.study.entity.DailyTask;
import com.chineselearning.chineselearningapi.user.entity.User;
import com.chineselearning.chineselearningapi.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import com.chineselearning.chineselearningapi.lesson.entity.LessonSkill;
import com.chineselearning.chineselearningapi.study.entity.SkillType;
@Service
@RequiredArgsConstructor
public class SkillAnalyticsService {

    private final SkillProgressRepository skillProgressRepository;
    private final UserRepository userRepository;
    @Transactional
    public void recordQuizResult(
            User user,
            Integer hskLevel,
            LessonSkill lessonSkill,
            int totalQuestions,
            int correctAnswers
    ) {

        SkillType skillType =
                mapLessonSkillToSkillType(
                        lessonSkill
                );

        SkillProgress skillProgress =
                skillProgressRepository
                        .findByUserIdAndHskLevelAndSkill(
                                user.getId(),
                                hskLevel,
                                skillType
                        )
                        .orElseGet(() ->
                                SkillProgress.builder()
                                        .user(user)
                                        .hskLevel(hskLevel)
                                        .skill(skillType)
                                        .completedTasks(0)
                                        .completedMinutes(0)
                                        .exerciseAttempts(0)
                                        .correctAnswers(0)
                                        .averageScore(null)
                                        .build()
                        );

        int oldAttempts =
                skillProgress.getExerciseAttempts() == null
                        ? 0
                        : skillProgress.getExerciseAttempts();

        int oldCorrect =
                skillProgress.getCorrectAnswers() == null
                        ? 0
                        : skillProgress.getCorrectAnswers();

        int newAttempts =
                oldAttempts + totalQuestions;

        int newCorrect =
                oldCorrect + correctAnswers;

        skillProgress.setExerciseAttempts(
                newAttempts
        );

        skillProgress.setCorrectAnswers(
                newCorrect
        );

        double averageScore = 0.0;

        if (newAttempts > 0) {

            averageScore =
                    newCorrect * 100.0
                            / newAttempts;

            averageScore =
                    Math.round(
                            averageScore * 100.0
                    ) / 100.0;
        }

        skillProgress.setAverageScore(
                averageScore
        );

        skillProgressRepository.save(
                skillProgress
        );
    }
    @Transactional
    public void recordCompletedTask(
            User user,
            DailyTask task
    ) {

        SkillProgress progress =
                skillProgressRepository
                        .findByUserIdAndHskLevelAndSkill(
                                user.getId(),
                                task.getHskLevel(),
                                task.getSkill()
                        )
                        .orElseGet(() ->
                                SkillProgress.builder()
                                        .user(user)
                                        .hskLevel(task.getHskLevel())
                                        .skill(task.getSkill())
                                        .build()
                        );

        progress.setCompletedTasks(
                progress.getCompletedTasks() + 1
        );

        progress.setCompletedMinutes(
                progress.getCompletedMinutes()
                        + task.getMinutes()
        );

        skillProgressRepository.save(progress);
    }
    private SkillType mapLessonSkillToSkillType(
            LessonSkill lessonSkill
    ) {

        return switch (lessonSkill) {

            case VOCABULARY ->
                    SkillType.VOCABULARY;

            case LISTENING ->
                    SkillType.LISTENING;

            case SPEAKING ->
                    SkillType.SPEAKING;

            case READING ->
                    SkillType.READING;

            case WRITING ->
                    SkillType.WRITING;

            /*
             * Hiện SkillType chưa có GRAMMAR.
             * Tạm tính Grammar Quiz vào QUIZ.
             */
            case GRAMMAR ->
                    SkillType.QUIZ;
        };
    }
    @Transactional(readOnly = true)
    public SkillAnalyticsResponse getAnalytics(
            String email
    ) {

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );

        List<SkillProgressResponse> skills =
                skillProgressRepository
                        .findAllByUserIdOrderByHskLevelAsc(
                                user.getId()
                        )
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return SkillAnalyticsResponse.builder()
                .skills(skills)
                .build();
    }

    private SkillProgressResponse toResponse(
            SkillProgress progress
    ) {

        Double accuracy = null;

        if (progress.getExerciseAttempts() > 0) {

            accuracy =
                    progress.getCorrectAnswers()
                            * 100.0
                            / progress.getExerciseAttempts();
        }

        return SkillProgressResponse.builder()
                .hskLevel(progress.getHskLevel())
                .skill(progress.getSkill())
                .completedTasks(
                        progress.getCompletedTasks()
                )
                .completedMinutes(
                        progress.getCompletedMinutes()
                )
                .exerciseAttempts(
                        progress.getExerciseAttempts()
                )
                .correctAnswers(
                        progress.getCorrectAnswers()
                )
                .accuracy(accuracy)
                .averageScore(
                        progress.getAverageScore()
                )
                .build();
    }
}