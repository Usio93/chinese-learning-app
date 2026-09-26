package com.chineselearning.chineselearningapi.vocabulary.service;

import com.chineselearning.chineselearningapi.user.entity.User;
import com.chineselearning.chineselearningapi.user.entity.UserHskLevel;

import com.chineselearning.chineselearningapi.user.repository.UserHskLevelRepository;
import com.chineselearning.chineselearningapi.user.repository.UserRepository;

import com.chineselearning.chineselearningapi.vocabulary.dto.VocabularyReviewQueueItemResponse;
import com.chineselearning.chineselearningapi.vocabulary.dto.VocabularyReviewQueueResponse;

import com.chineselearning.chineselearningapi.vocabulary.entity.UserVocabulary;
import com.chineselearning.chineselearningapi.vocabulary.entity.Vocabulary;

import com.chineselearning.chineselearningapi.vocabulary.repository.UserVocabularyRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VocabularyReviewQueueService {

    private final UserRepository userRepository;

    private final UserHskLevelRepository
            userHskLevelRepository;

    private final UserVocabularyRepository
            userVocabularyRepository;

    // =========================================================
    // GET DUE REVIEW WORDS
    // =========================================================

    @Transactional(readOnly = true)
    public VocabularyReviewQueueResponse getDueReviews(
            String email,
            Integer limit
    ) {

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );

        // -----------------------------------------------------
        // 1. Get active HSK levels
        // -----------------------------------------------------

        Set<Integer> activeHskLevels =
                userHskLevelRepository
                        .findAllByUserIdAndActiveTrue(
                                user.getId()
                        )
                        .stream()
                        .map(
                                UserHskLevel::getHskLevel
                        )
                        .collect(
                                Collectors.toSet()
                        );

        if (activeHskLevels.isEmpty()) {

            return VocabularyReviewQueueResponse
                    .builder()
                    .dueCount(0)
                    .returnedCount(0)
                    .items(List.of())
                    .build();
        }

        // -----------------------------------------------------
        // 2. Get all words whose review time has arrived
        // -----------------------------------------------------

        List<UserVocabulary> dueWords =
                userVocabularyRepository
                        .findAllByUserIdAndNextReviewAtLessThanEqual(
                                user.getId(),
                                LocalDateTime.now()
                        )
                        .stream()

                        // Only HSK levels currently selected
                        .filter(userVocabulary -> {

                            Vocabulary vocabulary =
                                    userVocabulary
                                            .getVocabulary();

                            return activeHskLevels
                                    .contains(
                                            vocabulary
                                                    .getHskLevel()
                                    );
                        })

                        // Oldest due first
                        .sorted(
                                Comparator.comparing(
                                        UserVocabulary::getNextReviewAt,
                                        Comparator.nullsLast(
                                                Comparator.naturalOrder()
                                        )
                                )
                        )

                        .toList();

        int dueCount =
                dueWords.size();

        // -----------------------------------------------------
        // 3. Limit result
        // -----------------------------------------------------

        int safeLimit =
                limit == null
                        ? 20
                        : Math.max(
                        1,
                        Math.min(
                                limit,
                                100
                        )
                );

        List<VocabularyReviewQueueItemResponse>
                items =
                dueWords
                        .stream()
                        .limit(safeLimit)
                        .map(this::toResponse)
                        .toList();

        return VocabularyReviewQueueResponse
                .builder()

                .dueCount(
                        dueCount
                )

                .returnedCount(
                        items.size()
                )

                .items(
                        items
                )

                .build();
    }

    // =========================================================
    // MAPPER
    // =========================================================

    private VocabularyReviewQueueItemResponse
    toResponse(
            UserVocabulary userVocabulary
    ) {

        Vocabulary vocabulary =
                userVocabulary
                        .getVocabulary();

        return VocabularyReviewQueueItemResponse
                .builder()

                .vocabularyId(
                        vocabulary.getId()
                )

                .hanzi(
                        vocabulary.getHanzi()
                )

                .pinyin(
                        vocabulary.getPinyin()
                )

                .meaning(
                        vocabulary.getMeaningVi()
                )

                .hskLevel(
                        vocabulary.getHskLevel()
                )

                .status(
                        userVocabulary.getStatus()
                )

                .correctCount(
                        userVocabulary.getCorrectCount()
                )

                .wrongCount(
                        userVocabulary.getWrongCount()
                )

                .reviewCount(
                        userVocabulary.getReviewCount()
                )

                .lastReviewedAt(
                        userVocabulary.getLastReviewedAt()
                )

                .nextReviewAt(
                        userVocabulary.getNextReviewAt()
                )

                .build();
    }
}