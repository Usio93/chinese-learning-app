package com.chineselearning.chineselearningapi.vocabulary.controller;

import com.chineselearning.chineselearningapi.vocabulary.dto.DailyVocabularyResponse;
import com.chineselearning.chineselearningapi.vocabulary.service.DailyVocabularyService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vocabulary/daily")
@RequiredArgsConstructor
public class DailyVocabularyController {

    private final DailyVocabularyService
            dailyVocabularyService;

    @GetMapping("/today")
    public ResponseEntity<DailyVocabularyResponse>
    getToday(
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                dailyVocabularyService
                        .getToday(
                                authentication.getName()
                        )
        );
    }
}