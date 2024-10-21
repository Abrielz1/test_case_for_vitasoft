CREATE TABLE IF NOT EXISTS users
(
    id       BIGSERIAL PRIMARY KEY,
    email    VARCHAR(32) NOT NULL UNIQUE,
    password VARCHAR(1024) NOT NULl,
    username VARCHAR(128) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS tickets
(
    id        BIGSERIAL PRIMARY KEY,
    created   TIMESTAMP,
    message   VARCHAR(512) NOT NULL,
    status    VARCHAR(255) NOT NULL
        CONSTRAINT tickets_status_check
            CHECK ((status)::TEXT = ANY
                   ((ARRAY ['DRAFT'::CHARACTER VARYING, 'SEND'::CHARACTER VARYING, 'ACCEPTED'::CHARACTER VARYING, 'REJECTED'::CHARACTER VARYING])::TEXT[])),
    author_id BIGINT
        CONSTRAINT user_id
            REFERENCES users
);

create table if not exists user_roles
(
    user_id BIGINT       NOT NULL
        CONSTRAINT user_id
            REFERENCES users,
    roles   VARCHAR(255) NOT NULL
        CONSTRAINT user_roles_roles_check
            CHECK ((roles)::TEXT = ANY
                   ((ARRAY ['ROLE_USER'::CHARACTER VARYING, 'ROLE_ADMIN'::CHARACTER VARYING, 'ROLE_OPERATOR'::CHARACTER VARYING])::TEXT[])),
    PRIMARY KEY (user_id, roles)
);
