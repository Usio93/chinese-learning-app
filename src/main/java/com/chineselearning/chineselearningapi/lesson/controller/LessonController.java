package com.chineselearning.chineselearningapi.lesson.controller;

import com.chineselearning.chineselearningapi.lesson.dto.LessonResponse;
import com.chineselearning.chineselearningapi.lesson.service.LessonService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    // User lấy lesson theo các HSK đang chọn
    @GetMapping("/my")
    public ResponseEntity<List<LessonResponse>>
    getMyLessons(
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                lessonService.getMyLessons(
                        authentication.getName()
                )
        );
    }

    // Lấy lesson theo HSK cụ thể
    @GetMapping("/hsk/{hskLevel}")
    public ResponseEntity<List<LessonResponse>>
    getByHsk(
            @PathVariable
            Integer hskLevel
    ) {

        return ResponseEntity.ok(
                lessonService.getByHsk(
                        hskLevel
                )
        );
    }

    // Detail
    @GetMapping("/{lessonId}")
    public ResponseEntity<LessonResponse>
    getLesson(
            @PathVariable
            Long lessonId
    ) {

        return ResponseEntity.ok(
                lessonService.getById(
                        lessonId
                )
        );
    }
}