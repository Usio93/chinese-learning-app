package com.chineselearning.chineselearningapi.quiz.controller;

import com.chineselearning.chineselearningapi.quiz.dto.*;
import com.chineselearning.chineselearningapi.quiz.service.QuizService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    // =========================================================
    // QUIZ BY LESSON
    // =========================================================

    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<List<QuizResponse>>
    getByLesson(
            @PathVariable
            Long lessonId
    ) {

        return ResponseEntity.ok(
                quizService.getByLesson(
                        lessonId
                )
        );
    }

    // =========================================================
    // QUIZ DETAIL
    // =========================================================

    @GetMapping("/{quizId}")
    public ResponseEntity<QuizResponse>
    getQuiz(
            @PathVariable
            Long quizId
    ) {

        return ResponseEntity.ok(
                quizService.getQuiz(
                        quizId
                )
        );
    }

    // =========================================================
    // SUBMIT
    // =========================================================

    @PostMapping("/{quizId}/submit")
    public ResponseEntity<QuizResultResponse>
    submitQuiz(
            @PathVariable
            Long quizId,

            @Valid
            @RequestBody
            QuizSubmitRequest request,

            Authentication authentication
    ) {

        return ResponseEntity.ok(
                quizService.submitQuiz(
                        authentication.getName(),
                        quizId,
                        request
                )
        );
    }

    // =========================================================
    // HISTORY
    // =========================================================

    @GetMapping("/my/attempts")
    public ResponseEntity<
            List<QuizAttemptResponse>
            >
    getMyAttempts(
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                quizService.getMyAttempts(
                        authentication.getName()
                )
        );
    }
}