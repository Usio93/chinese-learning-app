package com.chineselearning.chineselearningapi.auth.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class AuthResponse {

    private String accessToken;

    private String refreshToken;

    private String tokenType;

    private Long userId;

    private String email;

    private String fullName;

    private Set<String> roles;

    private Boolean onboardingCompleted;
}