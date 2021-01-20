--liquibase formatted sql

--changeset Jessus:1611087079061-1
CREATE SEQUENCE hibernate_sequence;

--changeset Jessus:1611087079061-2
CREATE TABLE audience (id BIGINT NOT NULL, name VARCHAR(255), CONSTRAINT audience_pkey PRIMARY KEY (id));

--changeset Jessus:1611087079061-3
CREATE TABLE daily_parameters (id BIGINT NOT NULL, day VARCHAR(255), parameters_date VARCHAR NOT NULL, semester VARCHAR(255), week VARCHAR(255), CONSTRAINT daily_parameters_pkey PRIMARY KEY (id));

--changeset Jessus:1611087079061-4
CREATE TABLE "group" (id BIGINT NOT NULL, name VARCHAR(255), CONSTRAINT group_pkey PRIMARY KEY (id));

--changeset Jessus:1611087079061-5
CREATE TABLE hibernate_sequences (sequence_name VARCHAR(255) NOT NULL, next_val BIGINT, CONSTRAINT hibernate_sequences_pkey PRIMARY KEY (sequence_name));

--changeset Jessus:1611087079061-6
CREATE TABLE lesson (id BIGINT NOT NULL, cours_name VARCHAR(255), cours_office VARCHAR(255), day_number INTEGER, group_name VARCHAR(255), lesson_number VARCHAR(255), sub_group_name VARCHAR(255), teacher_name VARCHAR(255), CONSTRAINT lesson_pkey PRIMARY KEY (id));

--changeset Jessus:1611087079061-7
CREATE TABLE teacher (id BIGINT NOT NULL, name VARCHAR(255), CONSTRAINT teacher_pkey PRIMARY KEY (id));

--changeset Jessus:1611087079061-8
CREATE TABLE "user" (id BIGINT NOT NULL, active BOOLEAN, admin_entity BOOLEAN, chat_id BIGINT, f_name VARCHAR(255), l_name VARCHAR(255), user_group VARCHAR(255), user_language VARCHAR(255), user_nick_name VARCHAR(255), CONSTRAINT user_pkey PRIMARY KEY (id));

