package com.chineselearning.chineselearningapi.vocabulary.service;

import com.chineselearning.chineselearningapi.user.entity.User;
import com.chineselearning.chineselearningapi.user.repository.UserRepository;
import com.chineselearning.chineselearningapi.vocabulary.dto.*;
import com.chineselearning.chineselearningapi.vocabulary.entity.*;
import com.chineselearning.chineselearningapi.vocabulary.repository.*;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VocabularyService {

    private final UserRepository userRepository;

    private final VocabularyRepository
            vocabularyRepository;

    private final UserVocabularyRepository
            userVocabularyRepository;

    /*
     * Daily Vocabulary service
     * Dùng để đánh dấu từ hôm nay đã hoàn thành
     * sau khi user review đúng/sai.
     */
    private final DailyVocabularyService
            dailyVocabularyService;

    // =========================================================
    // GET VOCABULARY BY HSK
    // =========================================================

    @Transactional(readOnly = true)
    public List<VocabularyResponse>
    getVocabularyByHsk(
            String email,
            Integer hskLevel
    ) {

        User user = getUser(email);

        return vocabularyRepository
                .findAllByHskLevelAndActiveTrue(
                        hskLevel
                )
                .stream()
                .map(vocabulary ->
                        toResponse(
                                user,
                                vocabulary
                        )
                )
                .toList();
    }

    // =========================================================
    // REVIEW VOCABULARY
    // =========================================================

    @Transactional
    public VocabularyResponse reviewVocabulary(
            String email,
            Long vocabularyId,
            VocabularyReviewRequest request
    ) {

        User user = getUser(email);

        Vocabulary vocabulary =
                vocabularyRepository
                        .findById(vocabularyId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Vocabulary not found"
                                )
                        );

        UserVocabulary userVocabulary =
                userVocabularyRepository
                        .findByUserIdAndVocabularyId(
                                user.getId(),
                                vocabularyId
                        )
                        .orElseGet(() ->
                                UserVocabulary.builder()
                                        .user(user)
                                        .vocabulary(vocabulary)
                                        .status(
                                                VocabularyStatus.NEW
                                        )
                                        .build()
                        );

        LocalDateTime now =
                LocalDateTime.now();

        /*
         * Lần đầu user học từ này
         */
        if (userVocabulary.getFirstLearnedAt()
                == null) {

            userVocabulary
                    .setFirstLearnedAt(now);
        }

        /*
         * Tăng số lần review
         */
        userVocabulary.setReviewCount(
                userVocabulary.getReviewCount()
                        + 1
        );

        userVocabulary.setLastReviewedAt(now);

        /*
         * Nếu trả lời đúng
         */
        if (Boolean.TRUE.equals(
                request.getCorrect()
        )) {

            userVocabulary.setCorrectCount(
                    userVocabulary.getCorrectCount()
                            + 1
            );

        }

        /*
         * Nếu trả lời sai
         */
        else {

            userVocabulary.setWrongCount(
                    userVocabulary.getWrongCount()
                            + 1
            );
        }

        /*
         * Cập nhật:
         *
         * NEW
         * LEARNING
         * FAMILIAR
         * MASTERED
         *
         * và lịch ôn tiếp theo
         */
        updateStatusAndNextReview(
                userVocabulary,
                request.getCorrect(),
                now
        );

        /*
         * Lưu trạng thái từ vựng của user
         */
        userVocabularyRepository.save(
                userVocabulary
        );

        /*
         * Nếu từ này nằm trong Daily Vocabulary hôm nay,
         * đánh dấu nó đã hoàn thành.
         */
        if (Boolean.TRUE.equals(request.getCorrect())) {

            dailyVocabularyService
                    .markVocabularyCompleted(
                            email,
                            vocabularyId
                    );
        }

        return toResponse(
                userVocabulary
        );
    }

    // =========================================================
    // SPACED REPETITION
    // =========================================================

    private void updateStatusAndNextReview(
            UserVocabulary uv,
            Boolean correct,
            LocalDateTime now
    ) {

        /*
         * Trả lời sai
         *
         * -> LEARNING
         * -> ôn lại sau 6 giờ
         */
        if (!Boolean.TRUE.equals(correct)) {

            uv.setStatus(
                    VocabularyStatus.LEARNING
            );

            uv.setNextReviewAt(
                    now.plusHours(6)
            );

            return;
        }

        int correctCount =
                uv.getCorrectCount();

        /*
         * Đúng >= 8 lần
         *
         * -> MASTERED
         * -> ôn lại sau 14 ngày
         */
        if (correctCount >= 8) {

            uv.setStatus(
                    VocabularyStatus.MASTERED
            );

            uv.setNextReviewAt(
                    now.plusDays(14)
            );

        }

        /*
         * Đúng >= 4 lần
         *
         * -> FAMILIAR
         * -> ôn lại sau 5 ngày
         */
        else if (correctCount >= 4) {

            uv.setStatus(
                    VocabularyStatus.FAMILIAR
            );

            uv.setNextReviewAt(
                    now.plusDays(5)
            );

        }

        /*
         * Đúng < 4 lần
         *
         * -> LEARNING
         * -> ôn lại sau 1 ngày
         */
        else {

            uv.setStatus(
                    VocabularyStatus.LEARNING
            );

            uv.setNextReviewAt(
                    now.plusDays(1)
            );
        }
    }

    // =========================================================
    // VOCABULARY STATISTICS
    // =========================================================

    @Transactional(readOnly = true)
    public VocabularyStatsResponse
    getStats(String email) {

        User user = getUser(email);

        long newWords =
                userVocabularyRepository
                        .countByUserIdAndStatus(
                                user.getId(),
                                VocabularyStatus.NEW
                        );

        long learning =
                userVocabularyRepository
                        .countByUserIdAndStatus(
                                user.getId(),
                                VocabularyStatus.LEARNING
                        );

        long familiar =
                userVocabularyRepository
                        .countByUserIdAndStatus(
                                user.getId(),
                                VocabularyStatus.FAMILIAR
                        );

        long mastered =
                userVocabularyRepository
                        .countByUserIdAndStatus(
                                user.getId(),
                                VocabularyStatus.MASTERED
                        );

        long total =
                newWords
                        + learning
                        + familiar
                        + mastered;

        return VocabularyStatsResponse.builder()
                .total(total)
                .newWords(newWords)
                .learning(learning)
                .familiar(familiar)
                .mastered(mastered)
                .build();
    }

    // =========================================================
    // GET CURRENT USER
    // =========================================================

    private User getUser(String email) {

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );
    }

    // =========================================================
    // CONVERT VOCABULARY -> RESPONSE
    // =========================================================

    private VocabularyResponse toResponse(
            User user,
            Vocabulary vocabulary
    ) {

        UserVocabulary uv =
                userVocabularyRepository
                        .findByUserIdAndVocabularyId(
                                user.getId(),
                                vocabulary.getId()
                        )
                        .orElse(null);

        return VocabularyResponse.builder()
                .id(
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
                .exampleSentence(
                        vocabulary.getExampleSentence()
                )
                .examplePinyin(
                        vocabulary.getExamplePinyin()
                )
                .exampleMeaning(
                        vocabulary.getExampleMeaningVi()
                )
                .audioUrl(
                        vocabulary.getAudioUrl()
                )
                .hskLevel(
                        vocabulary.getHskLevel()
                )
                .status(
                        uv == null
                                ? VocabularyStatus.NEW
                                : uv.getStatus()
                )
                .correctCount(
                        uv == null
                                ? 0
                                : uv.getCorrectCount()
                )
                .wrongCount(
                        uv == null
                                ? 0
                                : uv.getWrongCount()
                )
                .reviewCount(
                        uv == null
                                ? 0
                                : uv.getReviewCount()
                )
                .build();
    }

    // =========================================================
    // CONVERT USER VOCABULARY -> RESPONSE
    // =========================================================

    private VocabularyResponse toResponse(
            UserVocabulary uv
    ) {

        Vocabulary vocabulary =
                uv.getVocabulary();

        return VocabularyResponse.builder()
                .id(
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
                .exampleSentence(
                        vocabulary.getExampleSentence()
                )
                .examplePinyin(
                        vocabulary.getExamplePinyin()
                )
                .exampleMeaning(
                        vocabulary.getExampleMeaningVi()
                )
                .audioUrl(
                        vocabulary.getAudioUrl()
                )
                .hskLevel(
                        vocabulary.getHskLevel()
                )
                .status(
                        uv.getStatus()
                )
                .correctCount(
                        uv.getCorrectCount()
                )
                .wrongCount(
                        uv.getWrongCount()
                )
                .reviewCount(
                        uv.getReviewCount()
                )
                .build();
    }
}