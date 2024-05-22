package com.example.demo.dto.statistic;

public interface MonthlyStatsChatProjection {
    String getMonth();
    Integer getConversationCount();
    Integer getChatMessageCount();
}
