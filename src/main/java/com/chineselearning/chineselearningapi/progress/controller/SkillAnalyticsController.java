package com.chineselearning.chineselearningapi.progress.controller;

import com.chineselearning.chineselearningapi.progress.dto.SkillAnalyticsResponse;
import com.chineselearning.chineselearningapi.progress.service.SkillAnalyticsService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/progress/skills")
@RequiredArgsConstructor
public class SkillAnalyticsController {

    private final SkillAnalyticsService skillAnalyticsService;

    @GetMapping
    public ResponseEntity<SkillAnalyticsResponse>
    getSkillAnalytics(
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                skillAnalyticsService.getAnalytics(
                        authentication.getName()
                )
        );
    }
}