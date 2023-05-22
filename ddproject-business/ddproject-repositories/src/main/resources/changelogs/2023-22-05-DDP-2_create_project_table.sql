--liquibase formatted sql

--changeset i.smeyukha:DDP-2_create_project_table
CREATE TABLE project IF NOT EXISTS(
    id SERIAL PRIMARY KEY,
    title TEXT NOT NULL,
    description TEXT,
    status TEXT NOT NULL
)