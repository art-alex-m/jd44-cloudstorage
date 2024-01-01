insert into users
values ('00a35c6a-ffd1-4a17-8d53-2001b8a0e700', 'test@example.com',
        '$2a$10$3KaGQ4lsSWX1ufg8lnuDwumKXu/plv9i1UWswgVnFcCJk7l5me9mW', true) -- 123456
;

insert into authorities
values ('test@example.com', 'permission:file:create'),
       ('test@example.com', 'permission:file:download'),
       ('test@example.com', 'permission:file:delete'),
       ('test@example.com', 'permission:file:list'),
       ('test@example.com', 'permission:file:update')
;
