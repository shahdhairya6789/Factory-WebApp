-- liquibase formatted sql
-- changeset Jeet:0005

ALTER TABLE tblm_attendance
    MODIFY COLUMN attendance_user_image_name varchar(255) NULL,
    ADD COLUMN attendance_user_image_path varchar(255) NULL;