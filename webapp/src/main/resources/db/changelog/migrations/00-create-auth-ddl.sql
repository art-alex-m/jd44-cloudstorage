-- liquibase formatted sql
-- changeset artalexm:create-users-table
create table users
(
    id       uuid         not null primary key,
    username varchar(100) not null unique,
    password varchar(500) not null,
    enabled  boolean      not null
);

-- changeset artalexm:create-authorities-table
create table authorities
(
    username  varchar(100) not null,
    authority varchar(250) not null,
    constraint fk_authorities_users foreign key (username) references users (username)
);

create unique index ix_auth_username on authorities (username, authority);
