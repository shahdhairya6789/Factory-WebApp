-- liquibase formatted sql
-- changeset Jeet:0006
ALTER TABLE tblm_user
    MODIFY COLUMN is_active boolean DEFAULT true;