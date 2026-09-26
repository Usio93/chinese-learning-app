package com.chineselearning.chineselearningapi.vocabulary.controller;

import com.chineselearning.chineselearningapi.vocabulary.dto.VocabularyReviewQueueResponse;
import com.chineselearning.chineselearningapi.vocabulary.service.VocabularyReviewQueueService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vocabulary/review")
@RequiredArgsConstructor
public class VocabularyReviewQueueController {

    private final VocabularyReviewQueueService
            vocabularyReviewQueueService;

    @GetMapping("/due")
    public ResponseEntity<VocabularyReviewQueueResponse>
    getDueReviews(
            Authentication authentication,

            @RequestParam(
                    defaultValue = "20"
            )
            Integer limit
    ) {

        return ResponseEntity.ok(
                vocabularyReviewQueueService
                        .getDueReviews(
                                authentication.getName(),
                                limit
                        )
        );
    }
}