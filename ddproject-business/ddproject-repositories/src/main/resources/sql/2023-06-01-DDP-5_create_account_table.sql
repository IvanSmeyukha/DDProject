--liquibase formatted sql

--changeset i.smeyukha:DDP-5_create_account_table
CREATE TABLE IF NOT EXISTS account (
    id SERIAL PRIMARY KEY,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL
    )