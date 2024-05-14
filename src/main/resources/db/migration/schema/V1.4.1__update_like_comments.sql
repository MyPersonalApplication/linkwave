ALTER TABLE like_comments
    DROP CONSTRAINT like_comments_reply_comment_id_fkey,
    DROP COLUMN reply_comment_id;