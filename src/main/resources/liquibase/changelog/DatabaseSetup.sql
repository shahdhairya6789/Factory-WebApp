-- liquibase formatted sql
-- changeset Jeet:0001


-- the prefix of the tables indicate the type of tables we use
-- tbl stands for table and the next alphabet indicate the type of table
-- tbls for static,
-- tblm for master/main,
-- tblt for transactional/mapping,
-- tbla for audit

-- static tables
CREATE TABLE tbls_role (
                           id int NOT NULL AUTO_INCREMENT,
                           name varchar(255) NOT NULL,
                           display_name varchar(255) NOT NULL,
                           is_active boolean default true,
                           `order` int NOT NULL,
                           created_at timestamp NOT NULL default CURRENT_TIMESTAMP,
                           modified_at timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           PRIMARY KEY (id)
);
insert into tbls_role(name, display_name, `order`) values('admin', 'Admin', 1), ('merchant', 'Merchant', 2), ('employee', 'Employee', 3);

CREATE TABLE tbls_shift (
                            id int NOT NULL AUTO_INCREMENT,
                            name varchar(255) NOT NULL,
                            display_name varchar(255) NOT NULL,
                            is_active boolean default true,
                            `order` int NOT NULL,
                            created_at timestamp default CURRENT_TIMESTAMP,
                            modified_at timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            PRIMARY KEY (id)
);
insert into tbls_shift(name, display_name, `order`) values('afternoon', 'Afternoon', 1), ('morning', 'Morning', 2);

CREATE TABLE tbls_salary_type (
                                  id int NOT NULL AUTO_INCREMENT,
                                  name varchar(255) NOT NULL,
                                  display_name varchar(255) NOT NULL,
                                  is_active boolean default true,
                                  `order` int NOT NULL,
                                  created_at timestamp default CURRENT_TIMESTAMP,
                                  modified_at timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                  PRIMARY KEY (id)
);
insert into tbls_salary_type(name, display_name, `order`) values('oneMachineSalary', 'One Machine Salary', 1), ('twoMachineSalary', 'Two Machine Salary', 2);

-- master tables
CREATE TABLE tblm_user (
                           id int NOT NULL AUTO_INCREMENT,
                           name varchar(255) NOT NULL,
                           mobile_number varchar(255) NULL,
                           email varchar(255) NULL,
                           is_active boolean default true,
                           manager_id int NOT NULL,
                           created_by int NOT NULL,
                           modified_by int NOT NULL,
                           created_at timestamp default CURRENT_TIMESTAMP,
                           modified_at timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           PRIMARY KEY (id)
);

CREATE TABLE tblm_machine (
                              id int NOT NULL AUTO_INCREMENT,
                              name varchar(255) NOT NULL,
                              heads varchar(255) NULL,
                              area varchar(255) NULL,
                              is_active boolean default true,
                              user_id int NOT NULL,
                              created_by int NOT NULL,
                              modified_by int NOT NULL,
                              created_at timestamp default CURRENT_TIMESTAMP,
                              modified_at timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                              PRIMARY KEY (id)
);

CREATE TABLE tblm_attendance (
                                 id int NOT NULL AUTO_INCREMENT,
                                 attendance_date timestamp default CURRENT_TIMESTAMP,
                                 production varchar(255) NULL,
                                 dhaga varchar(255) NULL,
                                 attendance_user_image_size int NULL,
                                 attendance_user_image_name int NULL,
                                 user_id int NOT NULL,
                                 machine_id int NOT NULL,
                                 shift_id int NOT NULL,
                                 salary_type_id int NOT NULL,
                                 is_active boolean default true,
                                 created_by int NOT NULL,
                                 modified_by int NOT NULL,
                                 created_at timestamp default CURRENT_TIMESTAMP,
                                 modified_at timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                 PRIMARY KEY (id),
                                 CONSTRAINT fk_user_attendance FOREIGN KEY (user_id) REFERENCES tblm_user (id),
                                 CONSTRAINT fk_machine_attendance FOREIGN KEY (machine_id) REFERENCES tblm_machine (id),
                                 CONSTRAINT fk_shift_attendance FOREIGN KEY (shift_id) REFERENCES tbls_shift (id),
                                 CONSTRAINT fk_salary_type_attendance FOREIGN KEY (salary_type_id) REFERENCES tbls_salary_type (id)
);

-- mapping tables
CREATE TABLE tblt_user_role_mapping (
                                        id int NOT NULL AUTO_INCREMENT,
                                        user_id int NOT NULL,
                                        role_id int NOT NULL,
                                        created_at timestamp default CURRENT_TIMESTAMP,
                                        modified_at timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                        PRIMARY KEY (id),
                                        CONSTRAINT fk_user_user_role_mapping FOREIGN KEY (user_id) REFERENCES tblm_user (id),
                                        CONSTRAINT fk_role_user_role_mapping FOREIGN KEY (role_id) REFERENCES tbls_role (id)
);

CREATE TABLE tblt_user_salary_mapping (
                                          id int NOT NULL AUTO_INCREMENT,
                                          user_id int NOT NULL,
                                          salary int NOT NULL,
                                          salary_type_id int NOT NULL,
                                          salary_date timestamp default CURRENT_TIMESTAMP,
                                          created_by int NOT NULL,
                                          modified_by int NOT NULL,
                                          created_at timestamp default CURRENT_TIMESTAMP,
                                          modified_at timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                          PRIMARY KEY (id),
                                          CONSTRAINT fk_user_user_salary_mapping FOREIGN KEY (user_id) REFERENCES tblm_user (id),
                                          CONSTRAINT fk_salary_user_salary_mapping FOREIGN KEY (salary_type_id) REFERENCES tbls_salary_type (id)
);

CREATE TABLE tblt_user_advance_salary_mapping (
                                                  id int NOT NULL AUTO_INCREMENT,
                                                  user_id int NOT NULL,
                                                  advance_salary int NOT NULL,
                                                  salary_type_id int NOT NULL,
                                                  advance_salary_date timestamp default CURRENT_TIMESTAMP,
                                                  paid_by_user_id int NOT NULL,
                                                  created_by int NOT NULL,
                                                  modified_by int NOT NULL,
                                                  created_at timestamp default CURRENT_TIMESTAMP,
                                                  modified_at timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                                  PRIMARY KEY (id),
                                                  CONSTRAINT fk_user_user_advance_salary_mapping FOREIGN KEY (user_id) REFERENCES tblm_user (id),
                                                  CONSTRAINT fk_paid_by_user_user_advance_salary_mapping FOREIGN KEY (paid_by_user_id) REFERENCES tblm_user (id),
                                                  CONSTRAINT fk_salary_user_advance_salary_mapping FOREIGN KEY (salary_type_id) REFERENCES tbls_salary_type (id)
);

CREATE TABLE tblt_user_payment_mapping (
                                           id int NOT NULL AUTO_INCREMENT,
                                           user_id int NOT NULL,
                                           payment_amount int NOT NULL,
                                           advance_payment int NOT NULL,
                                           salary_type_id int NOT NULL,
                                           payment_date timestamp default CURRENT_TIMESTAMP,
                                           created_by int NOT NULL,
                                           modified_by int NOT NULL,
                                           created_at timestamp default CURRENT_TIMESTAMP,
                                           modified_at timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                           PRIMARY KEY (id),
                                           CONSTRAINT fk_user_user_payment_mapping FOREIGN KEY (user_id) REFERENCES tblm_user (id),
                                           CONSTRAINT fk_salary_user_payment_mapping FOREIGN KEY (salary_type_id) REFERENCES tbls_salary_type (id)
);

-- audit tables
CREATE TABLE tbla_user (
                           id int NOT NULL AUTO_INCREMENT,
                           name varchar(255) NOT NULL,
                           mobile_number varchar(255) NULL,
                           email varchar(255) NULL,
                           is_active boolean default true,
                           manager_id int NOT NULL,
                           user_id int NOT NULL,
                           created_by_user_id int NOT NULL,
                           modified_by_user_id int NOT NULL,
                           created_by int NOT NULL,
                           modified_by int NOT NULL,
                           created_at timestamp default CURRENT_TIMESTAMP,
                           modified_at timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           PRIMARY KEY (id),
                           CONSTRAINT fk_audit_user FOREIGN KEY (user_id) REFERENCES tblm_user (id)
);

CREATE TABLE tbla_user_salary_mapping (
                                          id int NOT NULL AUTO_INCREMENT,
                                          user_id int NOT NULL,
                                          salary_id int NOT NULL,
                                          salary int NOT NULL,
                                          salary_type_id int NOT NULL,
                                          salary_date timestamp default CURRENT_TIMESTAMP,
                                          created_by int NOT NULL,
                                          modified_by int NOT NULL,
                                          created_at timestamp default CURRENT_TIMESTAMP,
                                          modified_at timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                          PRIMARY KEY (id),
                                          CONSTRAINT fk_audit_salary_user_salary_mapping FOREIGN KEY (salary_id) REFERENCES tblt_user_salary_mapping (id),
                                          CONSTRAINT fk_audit_user_user_salary_mapping FOREIGN KEY (user_id) REFERENCES tblm_user (id),
                                          CONSTRAINT fk_audit_salary_type_user_salary_mapping FOREIGN KEY (salary_type_id) REFERENCES tbls_salary_type (id)
);