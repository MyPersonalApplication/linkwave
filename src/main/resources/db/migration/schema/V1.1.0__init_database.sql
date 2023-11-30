CREATE TABLE IF NOT EXISTS roles
(
    id   UUID PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS users
(
    id         UUID         PRIMARY KEY,
    email      VARCHAR(255) NOT NULL,
    is_active  BOOLEAN      NOT NULL,
    archived   BOOLEAN      NOT NULL    DEFAULT false,
    created_at TIMESTAMP    NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_roles
(
    user_id UUID,
    role_id UUID,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES roles (id)
);

CREATE TABLE IF NOT EXISTS user_profiles
(
    id              UUID            PRIMARY KEY REFERENCES users (id),
    gender          BOOLEAN,
    dob             DATE,
    country         VARCHAR(255),
    address         VARCHAR(255),
    about_me        VARCHAR(255),
    phone_number    VARCHAR(255),
    hobby           VARCHAR(255)[],
    avatar_url      TEXT,
    cover_url       TEXT,
    archived        BOOLEAN         NOT NULL    DEFAULT false,
    created_at      TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS user_skills
(
    id                 UUID         PRIMARY KEY,
    user_id            UUID         NOT NULL,
    skill_name         VARCHAR(255),
    certification_name VARCHAR(255),
    archived           BOOLEAN      NOT NULL    DEFAULT false,
    created_at         TIMESTAMP    NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP    NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id)
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
    archived                BOOLEAN       NOT NULL    DEFAULT false,
    created_at              TIMESTAMP     NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMP     NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS user_activities
(
    id          UUID         PRIMARY KEY,
    user_id     UUID         NOT NULL,
    target_id   UUID         NOT NULL,
    action      VARCHAR(255) NOT NULL,
    archived    BOOLEAN      NOT NULL   DEFAULT false,
    created_at  TIMESTAMP    NOT NULL   DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL   DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (target_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS notifications
(
    id         UUID         PRIMARY KEY,
    user_id    UUID         NOT NULL,
    message    TEXT         NOT NULL,
    is_read    BOOLEAN      NOT NULL,
    archived   BOOLEAN      NOT NULL    DEFAULT false,
    created_at TIMESTAMP    NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS albums
(
    id         UUID         PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    archived   BOOLEAN      NOT NULL    DEFAULT false,
    created_at TIMESTAMP    NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS album_images
(
    id          UUID         PRIMARY KEY,
    user_id     UUID         NOT NULL,
    album_id    UUID         NOT NULL,
    caption     TEXT,
    url         TEXT         NOT NULL,
    is_video    BOOLEAN      NOT NULL,
    archived    BOOLEAN      NOT NULL    DEFAULT false,
    created_at  TIMESTAMP    NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (album_id) REFERENCES albums (id)
);

CREATE TABLE IF NOT EXISTS friend_requests
(
    id          UUID        PRIMARY KEY,
    sender_id   UUID        NOT NULL,
    receiver_id UUID        NOT NULL,
    archived   BOOLEAN      NOT NULL    DEFAULT false,
    created_at TIMESTAMP    NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES users (id),
    FOREIGN KEY (receiver_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS friendships
(
    id         UUID         PRIMARY KEY,
    user_id    UUID         NOT NULL,
    friend_id  UUID         NOT NULL,
    archived   BOOLEAN      NOT NULL    DEFAULT false,
    created_at TIMESTAMP    NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (friend_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS conversations
(
    id         UUID         PRIMARY KEY,
    name       VARCHAR(255),
    archived   BOOLEAN      NOT NULL    DEFAULT false,
    created_at TIMESTAMP    NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS messages
(
    id              UUID        PRIMARY KEY,
    conversation_id UUID        NOT NULL,
    sender_id       UUID        NOT NULL,
    content         TEXT        NOT NULL,
    archived        BOOLEAN     NOT NULL    DEFAULT false,
    created_at      TIMESTAMP   NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP   NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (conversation_id) REFERENCES conversations (id),
    FOREIGN KEY (sender_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS participants
(
    id              UUID        PRIMARY KEY,
    conversation_id UUID        NOT NULL,
    user_id         UUID        NOT NULL,
    archived        BOOLEAN     NOT NULL    DEFAULT false,
    created_at      TIMESTAMP   NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP   NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (conversation_id) REFERENCES conversations (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS receipts
(
    id         UUID         PRIMARY KEY,
    message_id UUID         NOT NULL,
    user_id    UUID         NOT NULL,
    is_read    BOOLEAN      NOT NULL,
    archived   BOOLEAN      NOT NULL    DEFAULT false,
    created_at TIMESTAMP    NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (message_id) REFERENCES messages (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS messages_attachments
(
    id         UUID         PRIMARY KEY,
    message_id UUID         NOT NULL,
    file_url   VARCHAR(255) NOT NULL,
    archived   BOOLEAN      NOT NULL    DEFAULT false,
    created_at TIMESTAMP    NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (message_id) REFERENCES messages (id)
);

CREATE TABLE IF NOT EXISTS posts
(
    id         UUID         PRIMARY KEY,
    user_id    UUID         NOT NULL,
    content    TEXT         NOT NULL,
    archived   BOOLEAN      NOT NULL    DEFAULT false,
    created_at TIMESTAMP    NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS post_medias
(
    id         UUID         PRIMARY KEY,
    post_id    UUID         NOT NULL,
    url        VARCHAR(255) NOT NULL,
    is_video   BOOLEAN      NOT NULL,
    archived   BOOLEAN      NOT NULL    DEFAULT false,
    created_at TIMESTAMP    NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts (id)
);

CREATE TABLE IF NOT EXISTS post_likes
(
    id         UUID PRIMARY KEY,
    post_id    UUID      NOT NULL,
    user_id    UUID      NOT NULL,
    archived   BOOLEAN      NOT NULL    DEFAULT false,
    created_at TIMESTAMP    NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS post_comments
(
    id         UUID         PRIMARY KEY,
    post_id    UUID         NOT NULL,
    user_id    UUID         NOT NULL,
    content    TEXT         NOT NULL,
    archived   BOOLEAN      NOT NULL    DEFAULT false,
    created_at TIMESTAMP    NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS post_comment_replies
(
    id         UUID         PRIMARY KEY,
    comment_id UUID         NOT NULL,
    user_id    UUID         NOT NULL,
    content    TEXT         NOT NULL,
    archived   BOOLEAN      NOT NULL    DEFAULT false,
    created_at TIMESTAMP    NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (comment_id) REFERENCES post_comments (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS post_comment_likes
(
    id                  UUID         PRIMARY KEY,
    comment_id          UUID         NOT NULL,
    comment_reply_id    UUID         NOT NULL,
    user_id             UUID         NOT NULL,
    archived            BOOLEAN      NOT NULL   DEFAULT false,
    created_at          TIMESTAMP    NOT NULL   DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP    NOT NULL   DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (comment_id) REFERENCES post_comments (id),
    FOREIGN KEY (comment_reply_id) REFERENCES post_comment_replies (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
    );
