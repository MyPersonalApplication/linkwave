CREATE TABLE IF NOT EXISTS user_avatar
(
    user_id     UUID            PRIMARY KEY REFERENCES users (id),
    image_url   TEXT NULL,
    image_id    VARCHAR(255) NULL,
    archived    BOOLEAN      NOT NULL    DEFAULT false,
    created_at  TIMESTAMP    NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_cover
(
    user_id     UUID        PRIMARY KEY REFERENCES users (id),
    image_url   TEXT NULL,
    image_id    VARCHAR(255) NULL,
    archived    BOOLEAN      NOT NULL    DEFAULT false,
    created_at  TIMESTAMP    NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
