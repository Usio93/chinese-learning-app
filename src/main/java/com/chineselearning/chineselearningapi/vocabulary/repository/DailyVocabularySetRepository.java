package com.chineselearning.chineselearningapi.vocabulary.repository;

import com.chineselearning.chineselearningapi.vocabulary.entity.DailyVocabularySet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyVocabularySetRepository
        extends JpaRepository<DailyVocabularySet, Long> {

    Optional<DailyVocabularySet>
    findByUserIdAndStudyDate(
            Long userId,
            LocalDate studyDate
    );
}