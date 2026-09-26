package com.chineselearning.chineselearningapi.vocabulary.repository;

import com.chineselearning.chineselearningapi.vocabulary.entity.DailyVocabularyItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyVocabularyItemRepository
        extends JpaRepository<DailyVocabularyItem, Long> {
}