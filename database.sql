create database rank_up;

\c rank_up;

create table users
(
    chat_id       bigint not null,
    user_id       bigint not null,
    experience    bigint,
    first_name    varchar(255),
    language_code varchar(255),
    last_name     varchar(255),
    rank_level    integer,
    registered_at timestamp(6),
    user_name     varchar(255),
    primary key (chat_id, user_id)
);

create table chats
(
    id    bigint not null
        primary key,
    date  integer,
    title varchar(255)
);

create table images
(
    image_url varchar(255) not null
        primary key
);

create table ranks
(
    level            integer generated by default as identity
        primary key,
    experience       bigint,
    level_up_message varchar(255),
    name             varchar(255)
);

create table default_ranks
(
    level      integer not null
        primary key,
    experience bigint,
    name       varchar(255)
);