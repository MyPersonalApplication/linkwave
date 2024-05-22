package com.example.demo.repository;

import com.example.demo.dto.statistic.MonthlyStatsChatProjection;
import com.example.demo.dto.statistic.MonthlyStatsProjection;
import com.example.demo.dto.statistic.PercentagePostMediaStatsProjection;
import com.example.demo.model.interact.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.UUID;

public interface StatisticRepository extends JpaRepository<Post, UUID> {
    @Query(value = """
                WITH all_months AS (
                                SELECT '01' AS month UNION ALL
                                SELECT '02' UNION ALL
                                SELECT '03' UNION ALL
                                SELECT '04' UNION ALL
                                SELECT '05' UNION ALL
                                SELECT '06' UNION ALL
                                SELECT '07' UNION ALL
                                SELECT '08' UNION ALL
                                SELECT '09' UNION ALL
                                SELECT '10' UNION ALL
                                SELECT '11' UNION ALL
                                SELECT '12'
                            ),
                            post_counts AS (
                                SELECT
                                    TO_CHAR(p.created_at, 'MM') AS month,
                                    COUNT(*) AS post_count
                                FROM
                                    posts p
                                GROUP BY
                                    TO_CHAR(p.created_at, 'MM')
                            ),
                            post_like_counts AS (
                                SELECT
                                    TO_CHAR(pl.created_at, 'MM') AS month,
                                    COUNT(*) AS post_like_count
                                FROM
                                    post_likes pl
                                GROUP BY
                                    TO_CHAR(pl.created_at, 'MM')
                            ),
                            post_comment_counts AS (
                                SELECT
                                    TO_CHAR(pc.created_at, 'MM') AS month,
                                    COUNT(*) AS post_comment_count
                                FROM
                                    post_comments pc
                                GROUP BY
                                    TO_CHAR(pc.created_at, 'MM')
                            )
                            SELECT
                                am.month,
                                COALESCE(pc.post_count, 0) AS postCount,
                                COALESCE(plc.post_like_count, 0) AS postLikeCount,
                                COALESCE(pcc.post_comment_count, 0) AS postCommentCount
                            FROM
                                all_months am
                            LEFT JOIN
                                post_counts pc ON am.month = pc.month
                            LEFT JOIN
                                post_like_counts plc ON am.month = plc.month
                            LEFT JOIN
                                post_comment_counts pcc ON am.month = pcc.month
                            ORDER BY
                                am.month
            """, nativeQuery = true)
    List<MonthlyStatsProjection> getMonthlyStats();

    @Query(value = """
                SELECT
                    COUNT(DISTINCT u.id)
                FROM
                    users u
            """, nativeQuery = true)
    Integer getUserTotals();

    @Query(value = """
                WITH all_months AS (
                                SELECT '01' AS month UNION ALL
                                SELECT '02' UNION ALL
                                SELECT '03' UNION ALL
                                SELECT '04' UNION ALL
                                SELECT '05' UNION ALL
                                SELECT '06' UNION ALL
                                SELECT '07' UNION ALL
                                SELECT '08' UNION ALL
                                SELECT '09' UNION ALL
                                SELECT '10' UNION ALL
                                SELECT '11' UNION ALL
                                SELECT '12'
                            ),
                            conversation_counts AS (
                                SELECT
                                    TO_CHAR(c.created_at, 'MM') AS month,
                                    COUNT(*) AS conversation_count
                                FROM
                                    conversations c
                                GROUP BY
                                    TO_CHAR(c.created_at, 'MM')
                            ),
                            message_counts AS (
                                SELECT
                                    TO_CHAR(m.created_at, 'MM') AS month,
                                    COUNT(*) AS message_count
                                FROM
                                    messages m
                                GROUP BY
                                    TO_CHAR(m.created_at, 'MM')
                            )
                            SELECT
                                am.month,
                                COALESCE(cc.conversation_count, 0) AS conversationCount,
                                COALESCE(mc.message_count, 0) AS chatMessageCount
                            FROM
                                all_months am
                            LEFT JOIN
                                conversation_counts cc ON am.month = cc.month
                            LEFT JOIN
                                message_counts mc ON am.month = mc.month
                            ORDER BY
                                am.month
            """, nativeQuery = true)
    List<MonthlyStatsChatProjection> getMonthlyChatStats();

    @Query(value = """
                WITH post_media_counts AS (
                                SELECT COUNT(*) AS post_media_count
                                FROM posts p
                                LEFT JOIN post_media pm ON p.id = pm.post_id
                                WHERE pm.id IS NOT NULL
                            ),
                            post_without_media_counts AS (
                                SELECT COUNT(*) AS post_without_media_count
                                FROM posts p
                                LEFT JOIN post_media pm ON p.id = pm.post_id
                                WHERE pm.id IS NULL
                            )
                            SELECT
                                COALESCE(pmc.post_media_count, 0) AS withMediaCount,
                                COALESCE(pc.post_without_media_count, 0) AS withoutMediaCount
                            FROM
                                post_media_counts pmc
                            CROSS JOIN
                                post_without_media_counts pc
            """, nativeQuery = true)
    PercentagePostMediaStatsProjection getPercentagePostMedia();
}
