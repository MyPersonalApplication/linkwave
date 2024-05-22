package com.example.demo.controller;

import com.example.demo.dto.statistic.MonthlyStatsChatDTO;
import com.example.demo.dto.statistic.MonthlyStatsDTO;
import com.example.demo.dto.statistic.PercentagePostMediaDTO;
import com.example.demo.service.statistic.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class StatisticController {
    private final StatisticService statisticService;

    @GetMapping("/post/monthly")
    public ResponseEntity<List<MonthlyStatsDTO>> getMonthlyPostStats() {
        return ResponseEntity.ok(statisticService.getMonthlyPostStats());
    }

    @GetMapping("/user/totals")
    public ResponseEntity<Integer> getUserTotals() {
        return ResponseEntity.ok(statisticService.getUserTotals());
    }

    @GetMapping("/chat/monthly")
    public ResponseEntity<List<MonthlyStatsChatDTO>> getMonthlyChatStats() {
        return ResponseEntity.ok(statisticService.getMonthlyChatStats());
    }

    @GetMapping("/percentage-post-media")
    public ResponseEntity<PercentagePostMediaDTO> getPercentagePostMedia() {
        return ResponseEntity.ok(statisticService.getPercentagePostMedia());
    }
}
