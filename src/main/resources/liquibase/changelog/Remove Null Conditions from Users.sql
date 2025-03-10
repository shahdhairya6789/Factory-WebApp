-- liquibase formatted sql
-- changeset Jeet:0005

ALTER tblm_user
      MODIFY manager_id int,
      MODIFY created_by_user_id int,
      MODIFY modified_by_user_id int;