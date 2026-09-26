package com.chineselearning.chineselearningapi.onboarding.controller;

import com.chineselearning.chineselearningapi.onboarding.dto.OnboardingRequest;
import com.chineselearning.chineselearningapi.onboarding.dto.OnboardingResponse;
import com.chineselearning.chineselearningapi.onboarding.service.OnboardingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/onboarding")
@RequiredArgsConstructor
public class OnboardingController {

    private final OnboardingService onboardingService;

    @PostMapping
    public ResponseEntity<OnboardingResponse> completeOnboarding(
            Authentication authentication,
            @Valid @RequestBody OnboardingRequest request
    ) {

        return ResponseEntity.ok(
                onboardingService.completeOnboarding(
                        authentication.getName(),
                        request
                )
        );
    }
}