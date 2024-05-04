ALTER TABLE post_media
    DROP COLUMN url,
    DROP COLUMN caption,
    ADD COLUMN media_url TEXT NULL,
    ADD COLUMN media_id VARCHAR(255) NULL;