-- liquibase formatted sql
-- changeset Jeet:0002

ALTER TABLE tblm_user ADD COLUMN password varchar(255);