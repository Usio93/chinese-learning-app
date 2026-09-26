package com.chineselearning.chineselearningapi.study.controller;

import com.chineselearning.chineselearningapi.study.dto.StudyPlanResponse;
import com.chineselearning.chineselearningapi.study.service.StudyPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/study")
@RequiredArgsConstructor
public class StudyPlanController {

    private final StudyPlanService studyPlanService;

    @GetMapping("/today")
    public ResponseEntity<StudyPlanResponse> getTodayPlan(
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                studyPlanService.getTodayPlan(
                        authentication.getName()
                )
        );
    }

    @PostMapping("/tasks/{taskId}/complete")
    public ResponseEntity<StudyPlanResponse> completeTask(
            @PathVariable Long taskId,
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                studyPlanService.completeTask(
                        taskId,
                        authentication.getName()
                )
        );
    }
}