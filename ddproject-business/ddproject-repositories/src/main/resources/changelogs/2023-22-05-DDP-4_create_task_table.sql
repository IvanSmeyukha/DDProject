--liquibase formatted sql

--changeset i.smeyukha:DDP-4_create_task_table
CREATE TABLE task IF NOT EXISTS(
    id SERIAL PRIMARY KEY,
    title TEXT NOT NULL,
    description TEXT,
    executor_id BIGINT,
    project_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    labor_hours BIGINT NOT NULL,
    deadline TIMESTAMP WITH TIME ZONE NOT NULL,
    status TEXT NOT NULL,
    creation_date TIMESTAMP WITH TIME ZONE NOT NULL,
    last_update_date TIMESTAMP WITH TIME ZONE,
    FOREIGN KEY (executor_id) REFERENCES member(id),
    FOREIGN KEY (author_id) REFERENCES member(id),
    FOREIGN KEY (project_id) REFERENCES project(id)
)