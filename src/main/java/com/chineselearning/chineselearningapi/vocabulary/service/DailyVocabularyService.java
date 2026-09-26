package com.chineselearning.chineselearningapi.vocabulary.service;

import com.chineselearning.chineselearningapi.user.entity.User;
import com.chineselearning.chineselearningapi.user.entity.UserHskLevel;
import com.chineselearning.chineselearningapi.user.repository.UserHskLevelRepository;
import com.chineselearning.chineselearningapi.user.repository.UserRepository;
import com.chineselearning.chineselearningapi.vocabulary.dto.DailyVocabularyItemResponse;
import com.chineselearning.chineselearningapi.vocabulary.dto.DailyVocabularyResponse;
import com.chineselearning.chineselearningapi.vocabulary.entity.*;
import com.chineselearning.chineselearningapi.vocabulary.repository.*;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DailyVocabularyService {

    private final UserRepository userRepository;
    private final UserHskLevelRepository userHskLevelRepository;

    private final VocabularyRepository vocabularyRepository;
    private final UserVocabularyRepository userVocabularyRepository;

    private final DailyVocabularySetRepository dailyVocabularySetRepository;

    @Transactional
    public DailyVocabularyResponse getToday(
            String email
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );

        LocalDate today = LocalDate.now();

        DailyVocabularySet dailySet =
                dailyVocabularySetRepository
                        .findByUserIdAndStudyDate(
                                user.getId(),
                                today
                        )
                        .orElseGet(() ->
                                createDailySet(
                                        user,
                                        today
                                )
                        );

        return toResponse(
                user,
                dailySet
        );
    }

    private DailyVocabularySet createDailySet(
            User user,
            LocalDate date
    ) {

        int targetWords = 20;

        DailyVocabularySet dailySet =
                DailyVocabularySet.builder()
                        .user(user)
                        .studyDate(date)
                        .targetWords(targetWords)
                        .completedWords(0)
                        .build();

        List<DailyVocabularyItem> items =
                new ArrayList<>();

        // =========================================================
        // 1. LẤY HSK ĐANG ACTIVE
        // =========================================================

        List<UserHskLevel> activeLevels =
                userHskLevelRepository
                        .findAllByUserIdAndActiveTrue(
                                user.getId()
                        );

        if (activeLevels.isEmpty()) {
            throw new RuntimeException(
                    "No active HSK level"
            );
        }

        Set<Integer> activeHskLevels =
                activeLevels
                        .stream()
                        .map(
                                UserHskLevel::getHskLevel
                        )
                        .collect(
                                Collectors.toSet()
                        );

        // =========================================================
        // 2. LẤY TỪ ĐẾN HẠN ÔN
        // CHỈ LẤY HSK ĐANG ACTIVE
        // =========================================================

        List<UserVocabulary> reviewWords =
                userVocabularyRepository
                        .findAllByUserIdAndNextReviewAtLessThanEqual(
                                user.getId(),
                                LocalDateTime.now()
                        )
                        .stream()

                        .filter(uv ->
                                activeHskLevels.contains(
                                        uv.getVocabulary()
                                                .getHskLevel()
                                )
                        )

                        // từ quá hạn lâu nhất trước
                        .sorted(
                                Comparator.comparing(
                                        UserVocabulary::getNextReviewAt,
                                        Comparator.nullsLast(
                                                Comparator.naturalOrder()
                                        )
                                )
                        )

                        // tối đa 10 từ review/ngày
                        .limit(10)

                        .toList();

        Set<Long> selectedVocabularyIds =
                new HashSet<>();

        // =========================================================
        // 3. THÊM REVIEW WORDS TRƯỚC
        // =========================================================

        for (UserVocabulary uv :
                reviewWords) {

            Vocabulary vocabulary =
                    uv.getVocabulary();

            selectedVocabularyIds.add(
                    vocabulary.getId()
            );

            items.add(
                    DailyVocabularyItem.builder()
                            .dailySet(dailySet)
                            .vocabulary(vocabulary)
                            .review(true)
                            .completed(false)
                            .build()
            );
        }

        // =========================================================
        // 4. LẤY DANH SÁCH TỪ USER ĐÃ HỌC
        // =========================================================

        Set<Long> learnedVocabularyIds =
                userVocabularyRepository
                        .findAllByUserId(
                                user.getId()
                        )
                        .stream()
                        .map(uv ->
                                uv.getVocabulary()
                                        .getId()
                        )
                        .collect(
                                Collectors.toSet()
                        );

        // =========================================================
        // 5. TÍNH SỐ SLOT CÒN LẠI
        // =========================================================

        int remaining =
                targetWords - items.size();

        // =========================================================
        // 6. BỔ SUNG TỪ MỚI
        // =========================================================

        if (remaining > 0) {

            List<Vocabulary> candidates =
                    new ArrayList<>();

            for (UserHskLevel userHsk :
                    activeLevels) {

                candidates.addAll(
                        vocabularyRepository
                                .findAllByHskLevelAndActiveTrue(
                                        userHsk.getHskLevel()
                                )
                );
            }

            List<Vocabulary> newCandidates =
                    candidates
                            .stream()

                            // chưa từng học
                            .filter(v ->
                                    !learnedVocabularyIds
                                            .contains(
                                                    v.getId()
                                            )
                            )

                            // chưa có trong review hôm nay
                            .filter(v ->
                                    !selectedVocabularyIds
                                            .contains(
                                                    v.getId()
                                            )
                            )

                            .limit(remaining)

                            .toList();

            for (Vocabulary vocabulary :
                    newCandidates) {

                items.add(
                        DailyVocabularyItem.builder()
                                .dailySet(dailySet)
                                .vocabulary(vocabulary)
                                .review(false)
                                .completed(false)
                                .build()
                );

                selectedVocabularyIds.add(
                        vocabulary.getId()
                );
            }
        }

        // =========================================================
        // 7. SAVE
        // =========================================================

        dailySet.setItems(items);

        return dailyVocabularySetRepository
                .save(dailySet);
    }

    private DailyVocabularyResponse toResponse(
            User user,
            DailyVocabularySet dailySet
    ) {

        List<DailyVocabularyItemResponse> items =
                dailySet.getItems()
                        .stream()
                        .map(item ->
                                toItemResponse(
                                        user,
                                        item
                                )
                        )
                        .toList();

        int reviewWords =
                (int) items.stream()
                        .filter(
                                DailyVocabularyItemResponse::getReview
                        )
                        .count();

        int newWords =
                items.size() - reviewWords;

        int completed =
                (int) items.stream()
                        .filter(
                                DailyVocabularyItemResponse::getCompleted
                        )
                        .count();

        return DailyVocabularyResponse.builder()
                .date(
                        dailySet.getStudyDate()
                )
                .targetWords(
                        dailySet.getTargetWords()
                )
                .completedWords(completed)
                .remainingWords(
                        Math.max(
                                0,
                                items.size() - completed
                        )
                )
                .reviewWords(reviewWords)
                .newWords(newWords)
                .items(items)
                .build();
    }

    private DailyVocabularyItemResponse
    toItemResponse(
            User user,
            DailyVocabularyItem item
    ) {

        Vocabulary vocabulary =
                item.getVocabulary();

        UserVocabulary uv =
                userVocabularyRepository
                        .findByUserIdAndVocabularyId(
                                user.getId(),
                                vocabulary.getId()
                        )
                        .orElse(null);

        VocabularyStatus status =
                uv == null
                        ? VocabularyStatus.NEW
                        : uv.getStatus();

        return DailyVocabularyItemResponse.builder()
                .itemId(item.getId())
                .vocabularyId(
                        vocabulary.getId()
                )
                .hskLevel(
                        vocabulary.getHskLevel()
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
                .review(
                        item.getReview()
                )
                .completed(
                        item.getCompleted()
                )
                .status(status)
                .build();
    }
    @Transactional
    public void markVocabularyCompleted(
            String email,
            Long vocabularyId
    ) {

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );

        DailyVocabularySet dailySet =
                dailyVocabularySetRepository
                        .findByUserIdAndStudyDate(
                                user.getId(),
                                LocalDate.now()
                        )
                        .orElse(null);

        if (dailySet == null) {
            return;
        }

        for (DailyVocabularyItem item :
                dailySet.getItems()) {

            if (item.getVocabulary()
                    .getId()
                    .equals(vocabularyId)) {

                item.setCompleted(true);
                break;
            }
        }

        int completed =
                (int) dailySet.getItems()
                        .stream()
                        .filter(item ->
                                Boolean.TRUE.equals(
                                        item.getCompleted()
                                )
                        )
                        .count();

        dailySet.setCompletedWords(completed);

        dailyVocabularySetRepository.save(
                dailySet
        );
    }
}