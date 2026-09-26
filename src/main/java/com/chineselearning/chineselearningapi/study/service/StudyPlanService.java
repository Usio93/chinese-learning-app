package com.chineselearning.chineselearningapi.study.service;

import com.chineselearning.chineselearningapi.study.dto.DailyTaskResponse;
import com.chineselearning.chineselearningapi.study.dto.StudyPlanResponse;

import com.chineselearning.chineselearningapi.study.entity.*;

import com.chineselearning.chineselearningapi.study.repository.DailyTaskRepository;
import com.chineselearning.chineselearningapi.study.repository.StudyPlanRepository;

import com.chineselearning.chineselearningapi.user.entity.User;
import com.chineselearning.chineselearningapi.user.entity.UserHskLevel;
import com.chineselearning.chineselearningapi.user.entity.UserProfile;
import com.chineselearning.chineselearningapi.progress.service.SkillAnalyticsService;
import com.chineselearning.chineselearningapi.user.repository.UserHskLevelRepository;
import com.chineselearning.chineselearningapi.user.repository.UserProfileRepository;
import com.chineselearning.chineselearningapi.user.repository.UserRepository;
import com.chineselearning.chineselearningapi.progress.service.ProgressService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyPlanService {

    private final UserRepository userRepository;
    private final ProgressService progressService;
    private final UserProfileRepository
            userProfileRepository;

    private final UserHskLevelRepository
            userHskLevelRepository;
    private final SkillAnalyticsService skillAnalyticsService;
    private final StudyPlanRepository
            studyPlanRepository;

    private final DailyTaskRepository
            dailyTaskRepository;

    @Transactional
    public StudyPlanResponse getTodayPlan(
            String email
    ) {

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );

        UserProfile profile =
                userProfileRepository
                        .findByUserId(user.getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Complete onboarding first"
                                )
                        );

        List<UserHskLevel> activeHskLevels =
                userHskLevelRepository
                        .findAllByUserIdAndActiveTrue(
                                user.getId()
                        );

        if (activeHskLevels.isEmpty()) {

            throw new RuntimeException(
                    "Please select at least one HSK level"
            );
        }

        LocalDate today = LocalDate.now();

        StudyPlan plan =
                studyPlanRepository
                        .findByUserIdAndPlanDate(
                                user.getId(),
                                today
                        )
                        .orElseGet(() ->
                                createStudyPlan(
                                        user,
                                        profile,
                                        activeHskLevels,
                                        today
                                )
                        );

        return toResponse(plan);
    }

    private StudyPlan createStudyPlan(
            User user,
            UserProfile profile,
            List<UserHskLevel> hskLevels,
            LocalDate date
    ) {

        int totalMinutes =
                profile.getDailyStudyMinutes();

        StudyPlan plan =
                StudyPlan.builder()
                        .user(user)
                        .planDate(date)
                        .totalMinutes(totalMinutes)
                        .completedMinutes(0)
                        .build();

        List<DailyTask> tasks =
                generateTasks(
                        plan,
                        hskLevels,
                        totalMinutes
                );

        plan.setTasks(tasks);

        return studyPlanRepository.save(plan);
    }

    private List<DailyTask> generateTasks(
            StudyPlan plan,
            List<UserHskLevel> hskLevels,
            int totalMinutes
    ) {

        List<DailyTask> tasks =
                new ArrayList<>();

        int numberOfLevels =
                hskLevels.size();

        int baseMinutes =
                totalMinutes / numberOfLevels;

        int remainder =
                totalMinutes % numberOfLevels;

        for (int i = 0;
             i < numberOfLevels;
             i++) {

            int hskMinutes =
                    baseMinutes;

            if (i < remainder) {
                hskMinutes++;
            }

            int hskLevel =
                    hskLevels
                            .get(i)
                            .getHskLevel();

            tasks.addAll(
                    generateTasksForHsk(
                            plan,
                            hskLevel,
                            hskMinutes
                    )
            );
        }

        return tasks;
    }

    private List<DailyTask> generateTasksForHsk(
            StudyPlan plan,
            int hskLevel,
            int totalMinutes
    ) {

        List<DailyTask> tasks =
                new ArrayList<>();

        int vocabulary =
                (int) Math.round(
                        totalMinutes * 0.20
                );

        int listening =
                (int) Math.round(
                        totalMinutes * 0.20
                );

        int speaking =
                (int) Math.round(
                        totalMinutes * 0.15
                );

        int reading =
                (int) Math.round(
                        totalMinutes * 0.15
                );

        int writing =
                (int) Math.round(
                        totalMinutes * 0.15
                );

        int used =
                vocabulary
                        + listening
                        + speaking
                        + reading
                        + writing;

        int quiz =
                Math.max(
                        0,
                        totalMinutes - used
                );

        tasks.add(
                createTask(
                        plan,
                        hskLevel,
                        SkillType.VOCABULARY,
                        vocabulary
                )
        );

        tasks.add(
                createTask(
                        plan,
                        hskLevel,
                        SkillType.LISTENING,
                        listening
                )
        );

        tasks.add(
                createTask(
                        plan,
                        hskLevel,
                        SkillType.SPEAKING,
                        speaking
                )
        );

        tasks.add(
                createTask(
                        plan,
                        hskLevel,
                        SkillType.READING,
                        reading
                )
        );

        tasks.add(
                createTask(
                        plan,
                        hskLevel,
                        SkillType.WRITING,
                        writing
                )
        );

        tasks.add(
                createTask(
                        plan,
                        hskLevel,
                        SkillType.QUIZ,
                        quiz
                )
        );

        return tasks;
    }

    private DailyTask createTask(
            StudyPlan plan,
            Integer hskLevel,
            SkillType skill,
            int minutes
    ) {

        return DailyTask.builder()
                .studyPlan(plan)
                .hskLevel(hskLevel)
                .skill(skill)
                .minutes(minutes)
                .status(TaskStatus.PENDING)
                .build();
    }

    @Transactional
    public StudyPlanResponse completeTask(
            Long taskId,
            String email
    ) {

        DailyTask task =
                dailyTaskRepository
                        .findById(taskId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Task not found"
                                )
                        );

        StudyPlan plan =
                task.getStudyPlan();

        if (!plan.getUser()
                .getEmail()
                .equalsIgnoreCase(email)) {

            throw new RuntimeException(
                    "You cannot modify this task"
            );
        }

        if (task.getStatus()
                == TaskStatus.COMPLETED) {

            return toResponse(plan);
        }

        task.setStatus(
                TaskStatus.COMPLETED
        );

        dailyTaskRepository.save(task);

        plan.setCompletedMinutes(
                plan.getCompletedMinutes()
                        + task.getMinutes()
        );

        studyPlanRepository.save(plan);
        progressService.syncTodayProgress(
                plan.getUser(),
                plan
        );
        skillAnalyticsService.recordCompletedTask(
                plan.getUser(),
                task
        );

        return toResponse(plan);
    }

    private StudyPlanResponse toResponse(
            StudyPlan plan
    ) {

        List<DailyTaskResponse> tasks =
                plan.getTasks()
                        .stream()
                        .map(task ->
                                DailyTaskResponse.builder()
                                        .id(task.getId())
                                        .hskLevel(
                                                task.getHskLevel()
                                        )
                                        .skill(
                                                task.getSkill()
                                        )
                                        .minutes(
                                                task.getMinutes()
                                        )
                                        .status(
                                                task.getStatus()
                                        )
                                        .build()
                        )
                        .toList();

        Set<Integer> hskLevels =
                plan.getTasks()
                        .stream()
                        .map(
                                DailyTask::getHskLevel
                        )
                        .collect(
                                Collectors.toCollection(
                                        TreeSet::new
                                )
                        );

        int progress = 0;

        if (plan.getTotalMinutes() > 0) {

            progress =
                    (int) Math.round(
                            plan.getCompletedMinutes()
                                    * 100.0
                                    / plan.getTotalMinutes()
                    );
        }

        return StudyPlanResponse.builder()
                .planId(plan.getId())
                .date(plan.getPlanDate())
                .hskLevels(hskLevels)
                .totalMinutes(
                        plan.getTotalMinutes()
                )
                .completedMinutes(
                        plan.getCompletedMinutes()
                )
                .progressPercent(progress)
                .tasks(tasks)
                .build();
    }
}