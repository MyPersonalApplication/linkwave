package com.example.demo.service.statistic;

import com.example.demo.dto.statistic.MonthlyStatsChatDTO;
import com.example.demo.dto.statistic.MonthlyStatsDTO;
import com.example.demo.dto.statistic.PercentagePostMediaDTO;

import java.util.List;

public interface StatisticService {
    List<MonthlyStatsDTO> getMonthlyPostStats();

    Integer getUserTotals();

    List<MonthlyStatsChatDTO> getMonthlyChatStats();

    PercentagePostMediaDTO getPercentagePostMedia();
}
