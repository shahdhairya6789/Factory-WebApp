-- liquibase formatted sql
-- changeset Jeet:0003

ALTER TABLE tblm_user ADD COLUMN login_token varchar(255);