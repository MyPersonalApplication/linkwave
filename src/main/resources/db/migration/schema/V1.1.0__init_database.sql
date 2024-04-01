CREATE TABLE IF NOT EXISTS users
(
    id         UUID         PRIMARY KEY,
    email      VARCHAR(255) NOT NULL,
    is_active  BOOLEAN      NOT NULL,
    archived   BOOLEAN      NOT NULL    DEFAULT false,
    created_at TIMESTAMP    NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NULL    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_profile
(
    user_id         UUID            PRIMARY KEY REFERENCES users (id),
    gender          BOOLEAN,
    dob             DATE,
    country         VARCHAR(255),
    address         VARCHAR(255),
    about_me        TEXT,
    phone_number    VARCHAR(255),
    hobbies         VARCHAR(255),
    avatar_url      TEXT,
    cover_url       TEXT,
    archived        BOOLEAN         NOT NULL    DEFAULT false,
    created_at      TIMESTAMP       NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_skills
(
    id                 UUID         PRIMARY KEY,
    user_id            UUID         NOT NULL,
    skill_name         VARCHAR(255),
    certification_name VARCHAR(255),
    archived           BOOLEAN      NOT NULL    DEFAULT false,
    created_at         TIMESTAMP    NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP    NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_experiences
(
    id                      UUID          PRIMARY KEY,
    user_id                 UUID          NOT NULL,
    company_or_school_name  VARCHAR(255)  NOT NULL,
    position_or_degree      VARCHAR(255),
    description             VARCHAR(255),
    start_date              DATE,
    end_date                DATE,
    location                VARCHAR(255),
--     type: work, education
    experience_type         VARCHAR(50),
    archived                BOOLEAN       NOT NULL    DEFAULT false,
    created_at              TIMESTAMP     NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMP     NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_activities
(
    id          UUID         PRIMARY KEY,
    user_id     UUID         NOT NULL,
    target_id   UUID         NOT NULL,
--     action: follow, like, comment, reply, share, post, update_profile, update_experience, update_skill
    action      VARCHAR(255) NOT NULL,
    archived    BOOLEAN      NOT NULL   DEFAULT false,
    created_at  TIMESTAMP    NULL   DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NULL   DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (target_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS notifications
(
    id          UUID        PRIMARY KEY,
    sender_id   UUID        NOT NULL,
    receiver_id UUID        NOT NULL,
--     type: friend_request, message, post, comment, like, reply, follow
    notification_type       VARCHAR(50) NOT NULL,
    message     TEXT        NOT NULL,
    is_read     BOOLEAN     NOT NULL,
    archived    BOOLEAN     NOT NULL    DEFAULT false,
    created_at  TIMESTAMP   NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP   NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS albums
(
    id          UUID         PRIMARY KEY,
    user_id     UUID         NOT NULL,
    name        VARCHAR(255) NOT NULL,
    is_public   BOOLEAN      NOT NULL,
    archived    BOOLEAN      NOT NULL    DEFAULT false,
    created_at  TIMESTAMP    NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS album_media
(
    id          UUID         PRIMARY KEY,
    album_id    UUID         NOT NULL,
    caption     TEXT,
    url         TEXT         NOT NULL,
    is_video    BOOLEAN      NOT NULL,
    archived    BOOLEAN      NOT NULL    DEFAULT false,
    created_at  TIMESTAMP    NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (album_id) REFERENCES albums (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS friend_requests
(
    id          UUID        PRIMARY KEY,
    sender_id   UUID        NOT NULL,
    receiver_id UUID        NOT NULL,
    archived   BOOLEAN      NOT NULL    DEFAULT false,
    created_at TIMESTAMP    NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS friendships
(
    id         UUID         PRIMARY KEY,
    user_id    UUID         NOT NULL,
    friend_id  UUID         NOT NULL,
    archived   BOOLEAN      NOT NULL    DEFAULT false,
    created_at TIMESTAMP    NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS conversations
(
    id         UUID         PRIMARY KEY,
    name       VARCHAR(255),
    archived   BOOLEAN      NOT NULL    DEFAULT false,
    created_at TIMESTAMP    NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NULL    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS messages
(
    id              UUID        PRIMARY KEY,
    conversation_id UUID        NOT NULL,
    sender_id       UUID        NOT NULL,
    content         TEXT        NOT NULL,
    archived        BOOLEAN     NOT NULL    DEFAULT false,
    created_at      TIMESTAMP   NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP   NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (conversation_id) REFERENCES conversations (id) ON DELETE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS participants
(
    id              UUID        PRIMARY KEY,
    conversation_id UUID        NOT NULL,
    user_id         UUID        NOT NULL,
    archived        BOOLEAN     NOT NULL    DEFAULT false,
    created_at      TIMESTAMP   NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP   NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (conversation_id) REFERENCES conversations (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS receipts
(
    id         UUID         PRIMARY KEY,
    message_id UUID         NOT NULL,
    user_id    UUID         NOT NULL,
    is_read    BOOLEAN      NOT NULL,
    archived   BOOLEAN      NOT NULL    DEFAULT false,
    created_at TIMESTAMP    NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (message_id) REFERENCES messages (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS message_attachments
(
    id         UUID         PRIMARY KEY,
    message_id UUID         NOT NULL,
    file_url   TEXT         NOT NULL,
    file_name  VARCHAR(255) NOT NULL,
--     type: image, video, audio, document
    file_type  VARCHAR(255) NOT NULL,
    file_size  BIGINT       NOT NULL,
    archived   BOOLEAN      NOT NULL    DEFAULT false,
    created_at TIMESTAMP    NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (message_id) REFERENCES messages (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS posts
(
    id         UUID         PRIMARY KEY,
    user_id    UUID         NOT NULL,
    content    TEXT,
    archived   BOOLEAN      NOT NULL    DEFAULT false,
    created_at TIMESTAMP    NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS post_media
(
    id         UUID         PRIMARY KEY,
    post_id    UUID         NOT NULL,
    url        VARCHAR(255) NOT NULL,
    caption    TEXT,
    is_video   BOOLEAN      NOT NULL,
    archived   BOOLEAN      NOT NULL    DEFAULT false,
    created_at TIMESTAMP    NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS post_likes
(
    id         UUID PRIMARY KEY,
    post_id    UUID      NOT NULL,
    user_id    UUID      NOT NULL,
    archived   BOOLEAN      NOT NULL    DEFAULT false,
    created_at TIMESTAMP    NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS post_comments
(
    id         UUID         PRIMARY KEY,
    post_id    UUID         NOT NULL,
    user_id    UUID         NOT NULL,
    content    TEXT         NOT NULL,
    archived   BOOLEAN      NOT NULL    DEFAULT false,
    created_at TIMESTAMP    NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS reply_comments
(
    id         UUID         PRIMARY KEY,
    comment_id UUID         NOT NULL,
    user_id    UUID         NOT NULL,
    content    TEXT         NOT NULL,
    archived   BOOLEAN      NOT NULL    DEFAULT false,
    created_at TIMESTAMP    NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (comment_id) REFERENCES post_comments (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS like_comments
(
    id                  UUID         PRIMARY KEY,
    post_comment_id     UUID,
    reply_comment_id    UUID,
    user_id             UUID         NOT NULL,
    archived            BOOLEAN      NOT NULL   DEFAULT false,
    created_at          TIMESTAMP    NULL   DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP    NULL   DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_comment_id) REFERENCES post_comments (id) ON DELETE CASCADE,
    FOREIGN KEY (reply_comment_id) REFERENCES reply_comments (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
