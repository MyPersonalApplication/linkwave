ALTER TABLE user_experiences
    ALTER COLUMN description TYPE TEXT USING description::TEXT;