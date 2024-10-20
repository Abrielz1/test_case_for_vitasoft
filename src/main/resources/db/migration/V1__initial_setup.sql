CREATE TABLE flyway_schema_history
(
    installed_rank INTEGER                                   NOT NULL,
    version        VARCHAR(50),
    description    VARCHAR(200)                              NOT NULL,
    type           VARCHAR(20)                               NOT NULL,
    script         VARCHAR(1000)                             NOT NULL,
    checksum       INTEGER,
    installed_by   VARCHAR(100)                              NOT NULL,
    installed_on   TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    execution_time INTEGER                                   NOT NULL,
    success        BOOLEAN                                   NOT NULL,
    CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank)
);

CREATE INDEX flyway_schema_history_s_idx ON flyway_schema_history (success);

create table users
(
    id       bigserial primary key,
    email    varchar(32) not null unique,
    password varchar(32) not null,
    username varchar(32) not null unique
);

create table tickets
(
    id        bigserial primary key,
    created   timestamp    not null,
    message   varchar(512) not null,
    status    varchar(255) not null
        constraint tickets_status_check
            check ((status)::text = ANY
                   ((ARRAY ['DRAFT'::character varying, 'SEND'::character varying, 'ACCEPTED'::character varying, 'REJECTED'::character varying])::text[])),
    author_id bigint
        constraint user_id
            references users
);

create table user_roles
(
    user_id bigint       not null
        constraint user_id
            references users,
    roles   varchar(255) not null
        constraint user_roles_roles_check
            check ((roles)::text = ANY
                   ((ARRAY ['ROLE_USER'::character varying, 'ROLE_ADMIN'::character varying, 'ROLE_OPERATOR'::character varying])::text[])),
    primary key (user_id, roles)
);