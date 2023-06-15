--liquibase formatted sql

--changeset i.smeyukha:DDP-2_create_project_table
CREATE TABLE IF NOT EXISTS project(
    id BIGINT PRIMARY KEY,
    title TEXT NOT NULL,
    description TEXT,
    status TEXT NOT NULL
)