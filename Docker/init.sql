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
    created   timestamp,
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