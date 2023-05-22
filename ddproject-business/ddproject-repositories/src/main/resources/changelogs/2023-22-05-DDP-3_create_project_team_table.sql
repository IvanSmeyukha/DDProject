--liquibase formatted sql

--changeset i.smeyukha:DDP-3_create_project_team_table
CREATE TABLE project_team IF NOT EXISTS(
    member_id BIGINT,
    project_id BIGINT,
    role TEXT,
    PRIMARY KEY (project_id, member_id),
    FOREIGN KEY (member_id) REFERENCES member(id),
    FOREIGN KEY (project_id) REFERENCES project(id)
)