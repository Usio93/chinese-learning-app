package com.chineselearning.chineselearningapi.vocabulary.controller;

import com.chineselearning.chineselearningapi.vocabulary.dto.VocabularyDashboardResponse;
import com.chineselearning.chineselearningapi.vocabulary.dto.VocabularyHistoryResponse;
import com.chineselearning.chineselearningapi.vocabulary.service.VocabularyDashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vocabulary")
@RequiredArgsConstructor
public class VocabularyDashboardController {

    private final VocabularyDashboardService
            vocabularyDashboardService;

    // =========================================================
    // DASHBOARD
    // =========================================================

    @GetMapping("/dashboard")
    public ResponseEntity<VocabularyDashboardResponse>
    getDashboard(
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                vocabularyDashboardService
                        .getDashboard(
                                authentication.getName()
                        )
        );
    }

    // =========================================================
    // HISTORY
    // =========================================================

    @GetMapping("/history")
    public ResponseEntity<
            List<VocabularyHistoryResponse>
            >
    getHistory(
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                vocabularyDashboardService
                        .getHistory(
                                authentication.getName()
                        )
        );
    }
}