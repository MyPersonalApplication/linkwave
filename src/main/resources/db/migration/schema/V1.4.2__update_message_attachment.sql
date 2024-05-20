ALTER TABLE message_attachments
    DROP COLUMN file_size,
    ADD COLUMN file_id VARCHAR(255) NULL;