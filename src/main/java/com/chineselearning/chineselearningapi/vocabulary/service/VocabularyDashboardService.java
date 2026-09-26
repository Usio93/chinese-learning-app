package com.chineselearning.chineselearningapi.vocabulary.service;

import com.chineselearning.chineselearningapi.user.entity.User;
import com.chineselearning.chineselearningapi.user.repository.UserRepository;

import com.chineselearning.chineselearningapi.vocabulary.dto.VocabularyDashboardResponse;
import com.chineselearning.chineselearningapi.vocabulary.dto.VocabularyHistoryResponse;

import com.chineselearning.chineselearningapi.vocabulary.entity.DailyVocabularyItem;
import com.chineselearning.chineselearningapi.vocabulary.entity.DailyVocabularySet;
import com.chineselearning.chineselearningapi.vocabulary.entity.UserVocabulary;
import com.chineselearning.chineselearningapi.vocabulary.entity.Vocabulary;
import com.chineselearning.chineselearningapi.vocabulary.entity.VocabularyStatus;

import com.chineselearning.chineselearningapi.vocabulary.repository.DailyVocabularySetRepository;
import com.chineselearning.chineselearningapi.vocabulary.repository.UserVocabularyRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import com.chineselearning.chineselearningapi.user.entity.UserHskLevel;
import com.chineselearning.chineselearningapi.user.repository.UserHskLevelRepository;

import java.util.Set;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class VocabularyDashboardService {
    private final UserHskLevelRepository userHskLevelRepository;

    private final UserRepository userRepository;

    private final UserVocabularyRepository
            userVocabularyRepository;

    private final DailyVocabularySetRepository
            dailyVocabularySetRepository;

    // =========================================================
    // DASHBOARD
    // =========================================================

    @Transactional(readOnly = true)
    public VocabularyDashboardResponse getDashboard(
            String email
    ) {

        User user = getUser(email);

        Long userId = user.getId();

        // =====================================================
        // ALL TIME STATISTICS
        // =====================================================

        long totalLearned =
                userVocabularyRepository
                        .findAllByUserId(userId)
                        .size();

        long learning =
                userVocabularyRepository
                        .countByUserIdAndStatus(
                                userId,
                                VocabularyStatus.LEARNING
                        );

        long familiar =
                userVocabularyRepository
                        .countByUserIdAndStatus(
                                userId,
                                VocabularyStatus.FAMILIAR
                        );

        long mastered =
                userVocabularyRepository
                        .countByUserIdAndStatus(
                                userId,
                                VocabularyStatus.MASTERED
                        );

        Set<Integer> activeHskLevels =
                userHskLevelRepository
                        .findAllByUserIdAndActiveTrue(userId)
                        .stream()
                        .map(UserHskLevel::getHskLevel)
                        .collect(Collectors.toSet());

        long dueForReview =
                userVocabularyRepository
                        .findAllByUserIdAndNextReviewAtLessThanEqual(
                                userId,
                                LocalDateTime.now()
                        )
                        .stream()
                        .filter(userVocabulary ->
                                activeHskLevels.contains(
                                        userVocabulary
                                                .getVocabulary()
                                                .getHskLevel()
                                )
                        )
                        .count();

        // =====================================================
        // TODAY
        // =====================================================

        DailyVocabularySet todaySet =
                dailyVocabularySetRepository
                        .findByUserIdAndStudyDate(
                                userId,
                                LocalDate.now()
                        )
                        .orElse(null);

        int target = 0;
        int completed = 0;
        int remaining = 0;
        int newWords = 0;
        int reviewWords = 0;

        if (todaySet != null) {

            target =
                    todaySet.getTargetWords();

            completed =
                    (int) todaySet.getItems()
                            .stream()
                            .filter(item ->
                                    Boolean.TRUE.equals(
                                            item.getCompleted()
                                    )
                            )
                            .count();

            newWords =
                    (int) todaySet.getItems()
                            .stream()
                            .filter(item ->
                                    !Boolean.TRUE.equals(
                                            item.getReview()
                                    )
                            )
                            .count();

            reviewWords =
                    (int) todaySet.getItems()
                            .stream()
                            .filter(item ->
                                    Boolean.TRUE.equals(
                                            item.getReview()
                                    )
                            )
                            .count();

            /*
             * remaining tính theo số từ thực sự có trong set,
             * không lấy target - completed.
             *
             * Ví dụ:
             * target = 20
             * database mới có 2 từ
             * completed = 1
             *
             * remaining phải = 1
             * chứ không phải 19.
             */
            remaining =
                    Math.max(
                            0,
                            todaySet.getItems().size()
                                    - completed
                    );
        }

        double progressPercent = 0.0;

        if (todaySet != null
                && !todaySet.getItems().isEmpty()) {

            progressPercent =
                    (
                            completed * 100.0
                    )
                            / todaySet.getItems().size();
        }

        return VocabularyDashboardResponse.builder()

                .todayTarget(target)

                .todayCompleted(completed)

                .todayRemaining(remaining)

                .todayNewWords(newWords)

                .todayReviewWords(reviewWords)

                .totalLearned(totalLearned)

                .learning(learning)

                .familiar(familiar)

                .mastered(mastered)

                .dueForReview(dueForReview)

                .todayProgressPercent(
                        Math.round(
                                progressPercent * 100.0
                        ) / 100.0
                )

                .build();
    }

    // =========================================================
    // HISTORY
    // =========================================================

    @Transactional(readOnly = true)
    public List<VocabularyHistoryResponse> getHistory(
            String email
    ) {

        User user = getUser(email);

        return userVocabularyRepository
                .findAllByUserId(
                        user.getId()
                )
                .stream()

                /*
                 * Từ review gần nhất lên đầu.
                 *
                 * null -> cuối danh sách.
                 */
                .sorted(
                        Comparator.comparing(
                                UserVocabulary::getLastReviewedAt,
                                Comparator.nullsLast(
                                        Comparator.reverseOrder()
                                )
                        )
                )

                .map(this::toHistoryResponse)

                .toList();
    }

    // =========================================================
    // MAPPER
    // =========================================================

    private VocabularyHistoryResponse
    toHistoryResponse(
            UserVocabulary userVocabulary
    ) {

        Vocabulary vocabulary =
                userVocabulary.getVocabulary();

        return VocabularyHistoryResponse
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

                .firstLearnedAt(
                        userVocabulary.getFirstLearnedAt()
                )

                .lastReviewedAt(
                        userVocabulary.getLastReviewedAt()
                )

                .nextReviewAt(
                        userVocabulary.getNextReviewAt()
                )

                .build();
    }

    // =========================================================
    // GET USER
    // =========================================================

    private User getUser(
            String email
    ) {

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );
    }
}