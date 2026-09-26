package com.chineselearning.chineselearningapi.progress.controller;

import com.chineselearning.chineselearningapi.progress.dto.ProgressHistoryResponse;
import com.chineselearning.chineselearningapi.progress.dto.TodayProgressResponse;
import com.chineselearning.chineselearningapi.progress.service.ProgressService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;

    @GetMapping("/today")
    public ResponseEntity<TodayProgressResponse>
    getTodayProgress(
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                progressService.getTodayProgress(
                        authentication.getName()
                )
        );
    }

    @GetMapping("/history")
    public ResponseEntity<ProgressHistoryResponse>
    getHistory(
            Authentication authentication,
            @RequestParam(defaultValue = "7")
            int days
    ) {

        return ResponseEntity.ok(
                progressService.getHistory(
                        authentication.getName(),
                        days
                )
        );
    }
}