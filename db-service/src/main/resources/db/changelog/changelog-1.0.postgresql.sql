create schema parsing;
create schema users;

create table audience
(
    id   bigint not null
        constraint audience_pkey
            primary key,
    name varchar(255)
);

alter table audience
    owner to postgres;

create table "group"
(
    id   bigint not null
        constraint group_pkey
            primary key,
    name varchar(255)
);

alter table "group"
    owner to postgres;

create table lesson
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

create table teacher
(
    id   bigint not null
        constraint teacher_pkey
            primary key,
    name varchar(255)
);

alter table teacher
    owner to postgres;

create table daily_parameters
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



create table "user"
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