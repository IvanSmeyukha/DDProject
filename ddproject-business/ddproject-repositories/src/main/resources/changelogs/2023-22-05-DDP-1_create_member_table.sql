--liquibase formatted sql

--changeset i.smeyukha:DDP-1_create_member_table
CREATE TABLE member IF NOT EXISTS(
    id BIGINT SERIAL PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    sur_name TEXT,
    position TEXT,
    account BIGINT UNIQUE,
    email TEXT,
    status TEXT NOT NULL
);