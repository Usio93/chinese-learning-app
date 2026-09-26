package com.chineselearning.chineselearningapi.vocabulary.repository;

import com.chineselearning.chineselearningapi.vocabulary.entity.UserVocabulary;
import com.chineselearning.chineselearningapi.vocabulary.entity.VocabularyStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserVocabularyRepository
        extends JpaRepository<UserVocabulary, Long> {

    Optional<UserVocabulary>
    findByUserIdAndVocabularyId(
            Long userId,
            Long vocabularyId
    );

    List<UserVocabulary>
    findAllByUserId(
            Long userId
    );

    List<UserVocabulary>
    findAllByUserIdAndNextReviewAtLessThanEqual(
            Long userId,
            LocalDateTime dateTime
    );

    long countByUserIdAndStatus(
            Long userId,
            VocabularyStatus status
    );

    long countByUserIdAndNextReviewAtLessThanEqual(
            Long userId,
            LocalDateTime now
    );
}