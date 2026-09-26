package com.chineselearning.chineselearningapi.vocabulary.repository;

import com.chineselearning.chineselearningapi.vocabulary.entity.Vocabulary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VocabularyRepository
        extends JpaRepository<Vocabulary, Long> {

    List<Vocabulary>
    findAllByHskLevelAndActiveTrue(
            Integer hskLevel
    );
}