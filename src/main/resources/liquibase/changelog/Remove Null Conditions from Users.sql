-- liquibase formatted sql
-- changeset Jeet:0005

ALTER TABLE tblm_user
      MODIFY COLUMN manager_id int NULL;
--      MODIFY COLUMN created_by_user_id int NULL,
--      MODIFY COLUMN modified_by_user_id int NULL;