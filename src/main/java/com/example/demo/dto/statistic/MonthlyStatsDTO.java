package com.example.demo.dto.statistic;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MonthlyStatsDTO {
    @Id
    private String month;
    private Integer postCount;
    private Integer postLikeCount;
    private Integer postCommentCount;
}
