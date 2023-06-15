--liquibase formatted sql

--changeset i.smeyukha:DDP-6_alter_member_table_add_account_fk
ALTER TABLE IF EXISTS member
    ADD CONSTRAINT account_fk FOREIGN KEY (account) REFERENCES account(id)