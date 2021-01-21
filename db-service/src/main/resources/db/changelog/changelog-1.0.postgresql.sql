create schema if not exists parsing;
create schema if not exists users;

create table if not exists audience
(
    id   bigint not null
        constraint audience_pkey
            primary key,
    name varchar(255)
);

alter table audience
    owner to postgres;

create table if not exists "group"
(
    id   bigint not null
        constraint group_pkey
            primary key,
    name varchar(255)
);

alter table "group"
    owner to postgres;

create table if not exists lesson
(
    id             bigint not null
        constraint lesson_pkey
            primary key,
    cours_name     varchar(255),
    cours_office   varchar(255),
    day_number     integer,
    group_name     varchar(255),
    lesson_number  varchar(255),
    sub_group_name varchar(255),
    teacher_name   varchar(255)
);

alter table lesson
    owner to postgres;

create table if not exists teacher
(
    id   bigint not null
        constraint teacher_pkey
            primary key,
    name varchar(255)
);

alter table teacher
    owner to postgres;

create table if not exists daily_parameters
(
    id              bigint  not null
        constraint daily_parameters_pkey
            primary key,
    day             varchar(255),
    parameters_date varchar not null,
    semester        varchar(255),
    week            varchar(255)
);

alter table daily_parameters
    owner to postgres;



create table if not exists "user"
(
    id             bigint not null
        constraint user_pkey
            primary key,
    active         boolean,
    admin_entity   boolean,
    chat_id        bigint,
    f_name         varchar(255),
    l_name         varchar(255),
    user_group     varchar(255),
    user_language  varchar(255),
    user_nick_name varchar(255)
);

alter table "user"
    owner to postgres;

create schema if not exists history_data;

create table if not exists history_data.history
(
    id      bigint not null
        constraint history_pkey
            primary key,
    event   varchar(255),
    user_id bigint
        constraint fkn4gjyu69m6xa5f3bot571imbe
            references users."user"
);

alter table history_data.history
    owner to postgres;