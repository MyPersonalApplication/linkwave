CREATE TABLE roles (
    id      UUID PRIMARY KEY,
    name    VARCHAR(255)
);

CREATE TABLE users
(
    id          UUID         PRIMARY KEY ,
    email       VARCHAR(255) NOT NULL,
    is_active   BOOLEAN      NOT NULL,
    created_on  TIMESTAMP    NOT NULL
);

CREATE TABLE user_roles (
    user_id UUID,
    role_id UUID,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);
