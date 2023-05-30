--liquibase formatted sql

--changeset i.smeyukha:DDP-1_create_member_table
CREATE TABLE IF NOT EXISTS member(
    id SERIAL PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    patronymic TEXT,
    position TEXT,
    account BIGINT UNIQUE,
    email TEXT,
    status TEXT NOT NULL
);