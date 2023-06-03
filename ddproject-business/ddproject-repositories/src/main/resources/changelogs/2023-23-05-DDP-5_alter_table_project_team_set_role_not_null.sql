--liquibase formatted sql

--changeset i.smeyukha:DDP-5_alter_table_project_team_set_role_not_null
ALTER TABLE IF EXISTS project_team
    ALTER COLUMN role SET NOT NULL;