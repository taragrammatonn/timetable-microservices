create schema if not exists parsing;
create schema if not exists users;

create table if not exists parsing.audience
(
    id   bigint not null,
    name varchar(255),
    constraint audience_pkey
        primary key (id)
);

alter table audience
    owner to postgres;

create table if not exists parsing.daily_parameters
(
    id              bigint       not null,
    day             varchar(255),
    parameters_date varchar(255) not null,
    semester        varchar(255),
    week            varchar(255),
    constraint daily_parameters_pkey
        primary key (id)
);

alter table parsing.daily_parameters
    owner to postgres;

create table if not exists parsing."group"
(
    id   bigint not null,
    name varchar(255),
    constraint group_pkey
        primary key (id)
);

alter table parsing."group"
    owner to postgres;

create table if not exists parsing.lesson
(
    id             bigint not null,
    cours_name     varchar(255),
    cours_office   varchar(255),
    day_number     integer,
    group_name     varchar(255),
    lesson_number  varchar(255),
    sub_group_name varchar(255),
    teacher_name   varchar(255),
    constraint lesson_pkey
        primary key (id)
);

alter table parsing.lesson
    owner to postgres;

create table if not exists parsing.teacher
(
    id   bigint not null,
    name varchar(255),
    constraint teacher_pkey
        primary key (id)
);

alter table parsing.teacher
    owner to postgres;



create table if not exists users."user"
(
    id             bigint not null,
    active         boolean,
    admin_entity   boolean,
    chat_id        bigint,
    f_name         varchar(255),
    l_name         varchar(255),
    user_group     varchar(255),
    user_language  varchar(255),
    user_nick_name varchar(255),
    constraint user_pkey
        primary key (id),
    constraint uk_2x9mn7ijqx1uaori01f3w9hoj
        unique (chat_id)
);

alter table users."user"
    owner to postgres;

create table if not exists history_data.history
(
    id              bigint not null,
    event           varchar(255),
    user_id         bigint,
    request_date    varchar(255),
    request_message varchar(255),
    user_chat_id    bigint,
    chat_id         bigint,
    constraint history_pkey
        primary key (id),
    constraint fkn4gjyu69m6xa5f3bot571imbe
        foreign key (user_id) references users."user",
    constraint fk4prakw3qr3295fc83u2xht98n
        foreign key (chat_id) references users."user"
);

alter table history_data.history
    owner to postgres;

