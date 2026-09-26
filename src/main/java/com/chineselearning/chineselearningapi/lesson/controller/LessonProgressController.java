package com.chineselearning.chineselearningapi.lesson.controller;

import com.chineselearning.chineselearningapi.lesson.dto.LessonDetailResponse;
import com.chineselearning.chineselearningapi.lesson.dto.LessonProgressResponse;

import com.chineselearning.chineselearningapi.lesson.service.LessonContentService;
import com.chineselearning.chineselearningapi.lesson.service.LessonProgressService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lessons")
@RequiredArgsConstructor
public class LessonProgressController {

    private final LessonProgressService
            lessonProgressService;

    private final LessonContentService
            lessonContentService;

    @GetMapping("/{lessonId}/content")
    public ResponseEntity<LessonDetailResponse>
    getLessonContent(
            @PathVariable
            Long lessonId
    ) {

        return ResponseEntity.ok(
                lessonContentService
                        .getLessonContent(
                                lessonId
                        )
        );
    }

    @PostMapping("/{lessonId}/start")
    public ResponseEntity<LessonProgressResponse>
    startLesson(
            @PathVariable
            Long lessonId,

            Authentication authentication
    ) {

        return ResponseEntity.ok(
                lessonProgressService
                        .startLesson(
                                authentication.getName(),
                                lessonId
                        )
        );
    }

    @PostMapping("/{lessonId}/complete")
    public ResponseEntity<LessonProgressResponse>
    completeLesson(
            @PathVariable
            Long lessonId,

            Authentication authentication
    ) {

        return ResponseEntity.ok(
                lessonProgressService
                        .completeLesson(
                                authentication.getName(),
                                lessonId
                        )
        );
    }

    @GetMapping("/my/progress")
    public ResponseEntity<
            List<LessonProgressResponse>
            >
    getMyProgress(
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                lessonProgressService
                        .getMyProgress(
                                authentication.getName()
                        )
        );
    }
}