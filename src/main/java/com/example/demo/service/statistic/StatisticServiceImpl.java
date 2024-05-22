package com.example.demo.service.statistic;

import com.example.demo.dto.statistic.*;
import com.example.demo.repository.StatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private final StatisticRepository statisticRepository;

    @Override
    public List<MonthlyStatsDTO> getMonthlyPostStats() {
        List<MonthlyStatsProjection> monthlyStatsDTOs = statisticRepository.getMonthlyStats();

        return (List<MonthlyStatsDTO>) monthlyStatsDTOs.stream()
                .map(monthlyStatsDTO -> MonthlyStatsDTO.builder()
                        .month(monthlyStatsDTO.getMonth())
                        .postCount(monthlyStatsDTO.getPostCount())
                        .postLikeCount(monthlyStatsDTO.getPostLikeCount())
                        .postCommentCount(monthlyStatsDTO.getPostCommentCount())
                        .build())
                .toList();
    }

    @Override
    public Integer getUserTotals() {
        return statisticRepository.getUserTotals();
    }

    @Override
    public List<MonthlyStatsChatDTO> getMonthlyChatStats() {
        List<MonthlyStatsChatProjection> monthlyStatsChatDTOs = statisticRepository.getMonthlyChatStats();
        return (List<MonthlyStatsChatDTO>) monthlyStatsChatDTOs.stream()
                .map(monthlyStatsChatDTO -> MonthlyStatsChatDTO.builder()
                        .month(monthlyStatsChatDTO.getMonth())
                        .conversationCount(monthlyStatsChatDTO.getConversationCount())
                        .chatMessageCount(monthlyStatsChatDTO.getChatMessageCount())
                        .build())
                .toList();
    }

    @Override
    public PercentagePostMediaDTO getPercentagePostMedia() {
        PercentagePostMediaStatsProjection percentagePostMediaDTO = statisticRepository.getPercentagePostMedia();
        return PercentagePostMediaDTO.builder()
                .withMediaCount(percentagePostMediaDTO.getWithMediaCount())
                .withoutMediaCount(percentagePostMediaDTO.getWithoutMediaCount())
                .build();
    }
}
