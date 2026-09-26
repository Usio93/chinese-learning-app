package com.chineselearning.chineselearningapi.auth.service;

import com.chineselearning.chineselearningapi.auth.dto.*;
import com.chineselearning.chineselearningapi.auth.entity.RefreshToken;
import com.chineselearning.chineselearningapi.security.jwt.JwtService;
import com.chineselearning.chineselearningapi.security.service.CustomUserDetailsService;
import com.chineselearning.chineselearningapi.user.entity.AuthProvider;
import com.chineselearning.chineselearningapi.user.entity.Role;
import com.chineselearning.chineselearningapi.user.entity.RoleName;
import com.chineselearning.chineselearningapi.user.entity.User;
import com.chineselearning.chineselearningapi.user.repository.RoleRepository;
import com.chineselearning.chineselearningapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final CustomUserDetailsService userDetailsService;

    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {

        String email = request.getEmail()
                .trim()
                .toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException(
                    "Email is already registered"
            );
        }

        Role userRole = roleRepository
                .findByName(RoleName.ROLE_USER)
                .orElseThrow(() ->
                        new RuntimeException(
                                "ROLE_USER does not exist"
                        )
                );

        User user = User.builder()
                .email(email)
                .password(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )
                .fullName(request.getFullName().trim())
                .provider(AuthProvider.LOCAL)
                .enabled(true)
                .locked(false)
                .onboardingCompleted(false)
                .roles(Set.of(userRole))
                .build();

        user = userRepository.save(user);

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(
                        user.getEmail()
                );

        String accessToken =
                jwtService.generateAccessToken(userDetails);

        RefreshToken refreshToken =
                refreshTokenService
                        .createRefreshToken(user);

        return buildAuthResponse(
                user,
                accessToken,
                refreshToken.getToken()
        );
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {

        String email = request.getEmail()
                .trim()
                .toLowerCase();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        request.getPassword()
                )
        );

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );

        user.setLastLoginAt(LocalDateTime.now());
        user.setLastActiveAt(LocalDateTime.now());

        userRepository.save(user);

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(
                        user.getEmail()
                );

        String accessToken =
                jwtService.generateAccessToken(userDetails);

        RefreshToken refreshToken =
                refreshTokenService
                        .createRefreshToken(user);

        return buildAuthResponse(
                user,
                accessToken,
                refreshToken.getToken()
        );
    }

    @Transactional
    public AuthResponse refresh(
            RefreshTokenRequest request
    ) {

        RefreshToken refreshToken =
                refreshTokenService.verifyRefreshToken(
                        request.getRefreshToken()
                );

        User user = refreshToken.getUser();

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(
                        user.getEmail()
                );

        String newAccessToken =
                jwtService.generateAccessToken(userDetails);

        return buildAuthResponse(
                user,
                newAccessToken,
                refreshToken.getToken()
        );
    }

    private AuthResponse buildAuthResponse(
            User user,
            String accessToken,
            String refreshToken
    ) {

        Set<String> roles =
                user.getRoles()
                        .stream()
                        .map(role ->
                                role.getName().name()
                        )
                        .collect(Collectors.toSet());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .roles(roles)
                .onboardingCompleted(
                        user.getOnboardingCompleted()
                )
                .build();
    }
}