ALTER TABLE albums
    DROP COLUMN user_id;

ALTER TABLE album_media
    ADD COLUMN user_id uuid NOT NULL,
    ADD CONSTRAINT fk_album_media_user FOREIGN KEY (user_id) REFERENCES users (id);
