package com.example.demo.dto.statistic;

public interface MonthlyStatsProjection {
    String getMonth();
    Integer getPostCount();
    Integer getPostLikeCount();
    Integer getPostCommentCount();
}
