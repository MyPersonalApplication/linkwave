ALTER TABLE user_profiles
    DROP COLUMN hobbies;

CREATE TABLE IF NOT EXISTS user_hobbies (
    user_id     UUID NOT NULL,
    hobby       VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user_profiles (user_id) ON DELETE CASCADE
);