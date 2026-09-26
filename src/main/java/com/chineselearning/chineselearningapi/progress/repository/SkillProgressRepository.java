package com.chineselearning.chineselearningapi.progress.repository;

import com.chineselearning.chineselearningapi.progress.entity.SkillProgress;
import com.chineselearning.chineselearningapi.study.entity.SkillType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SkillProgressRepository
        extends JpaRepository<SkillProgress, Long> {

    Optional<SkillProgress>
    findByUserIdAndHskLevelAndSkill(
            Long userId,
            Integer hskLevel,
            SkillType skill
    );

    List<SkillProgress>
    findAllByUserIdOrderByHskLevelAsc(
            Long userId
    );

    List<SkillProgress>
    findAllByUserIdAndHskLevel(
            Long userId,
            Integer hskLevel
    );
}