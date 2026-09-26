package com.chineselearning.chineselearningapi.vocabulary.controller;

import com.chineselearning.chineselearningapi.vocabulary.dto.*;
import com.chineselearning.chineselearningapi.vocabulary.service.VocabularyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vocabulary")
@RequiredArgsConstructor
public class VocabularyController {

    private final VocabularyService vocabularyService;

    @GetMapping("/hsk/{hskLevel}")
    public ResponseEntity<List<VocabularyResponse>>
    getByHsk(
            @PathVariable Integer hskLevel,
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                vocabularyService
                        .getVocabularyByHsk(
                                authentication.getName(),
                                hskLevel
                        )
        );
    }

    @PostMapping("/{vocabularyId}/review")
    public ResponseEntity<VocabularyResponse>
    review(
            @PathVariable Long vocabularyId,
            @Valid
            @RequestBody
            VocabularyReviewRequest request,
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                vocabularyService
                        .reviewVocabulary(
                                authentication.getName(),
                                vocabularyId,
                                request
                        )
        );
    }

    @GetMapping("/stats")
    public ResponseEntity<VocabularyStatsResponse>
    getStats(
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                vocabularyService
                        .getStats(
                                authentication.getName()
                        )
        );
    }
}