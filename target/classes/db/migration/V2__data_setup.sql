
insert
into
    users
(email, password, username)
values
    ('a11@mail.com', '$2a$10$SMbccof38pLdxuEdJF8sxuX7ZIiDA8lkhN4.ufxaV6YNbr.Nu8cWG', 'vasya');

insert
into
    user_roles
(user_id, roles)
values
    (1, 'ROLE_ADMIN');

insert
into
    users
(email, password, username)
values
    ('oleg1@mail.com', '$2a$10$cvEXJ0eB7ljX/ntO0k7oR.up/rWS1sXBhsvNSjCMA8MYqaBFQljXC', 'oleg');

insert
into
    user_roles
(user_id, roles)
values
    (2, 'ROLE_OPERATOR');

insert
into
    users
(email, password, username)
values
    ('senya1@mail.com', '$2a$10$ZgRRrowx5a.OY.T2Y/h0O.2mHnCAItWVK0Vvh/hUG/dc78nvaiNiy', 'senya');

insert
into
    user_roles
(user_id, roles)
values
    (3, 'ROLE_USER');