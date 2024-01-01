-- liquibase formatted sql
-- changeset artalexm:insert-demo-user
insert into users
values ('09b35c6a-ffd1-4a17-8d53-2001b8a0e519', 'user@example.com',
        '$2a$10$3KaGQ4lsSWX1ufg8lnuDwumKXu/plv9i1UWswgVnFcCJk7l5me9mW', true);

insert into authorities
values ('user@example.com', 'permission:file:create'),
       ('user@example.com', 'permission:file:download'),
       ('user@example.com', 'permission:file:delete'),
       ('user@example.com', 'permission:file:list'),
       ('user@example.com', 'permission:file:update');