-- liquibase formatted sql
-- changeset Jeet:0001


-- the prefix of the tables indicate the type of tables we use
-- tbl stands for table and the next alphabet indicate the type of table
-- tbls for static,
-- tblm for master/main,
-- tblt for transactional/mapping,
-- tbla for audit

-- static tables
create table if not exists tbls_role
(
    id           int          NOT NULL AUTO_INCREMENT,
    name         varchar(255) NOT NULL,
    display_name varchar(255) NOT NULL,
    is_active    boolean               default true,
    `order`      int          NOT NULL,
    created_at   timestamp    NOT NULL default CURRENT_TIMESTAMP,
    modified_at  timestamp             default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
insert into tbls_role(name, display_name, `order`)
values ('ADMIN', 'Admin', 1),
       ('MERCHANT', 'Merchant', 2),
       ('EMPLOYEE', 'Employee', 3);

create table if not exists tbls_shift
(
    id           int          NOT NULL AUTO_INCREMENT,
    name         varchar(255) NOT NULL,
    display_name varchar(255) NOT NULL,
    is_active    boolean   default true,
    `order`      int          NOT NULL,
    created_at   timestamp default CURRENT_TIMESTAMP,
    modified_at  timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
insert into tbls_shift(name, display_name, `order`)
values ('afternoon', 'Afternoon', 1),
       ('morning', 'Morning', 2);

create table if not exists tbls_salary_type
(
    id           int          NOT NULL AUTO_INCREMENT,
    name         varchar(255) NOT NULL,
    display_name varchar(255) NOT NULL,
    is_active    boolean   default true,
    `order`      int          NOT NULL,
    created_at   timestamp default CURRENT_TIMESTAMP,
    modified_at  timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
insert into tbls_salary_type(name, display_name, `order`)
values ('oneMachineSalary', 'One Machine Salary', 1),
       ('twoMachineSalary', 'Two Machine Salary', 2);

-- master tables
create table if not exists tblm_user
(
    id            int          NOT NULL AUTO_INCREMENT,
    name          varchar(255) NOT NULL,
    mobile_number varchar(255) NULL,
    email         varchar(255) NULL,
    is_active     boolean   default true,
    manager_id    int          NULL,
    otp           varchar(255),
    password      varchar(255),
    login_token   varchar(255),
    created_by    int          NULL,
    modified_by   int          NULL,
    created_at    timestamp default CURRENT_TIMESTAMP,
    modified_at   timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

create table if not exists tblm_machine
(
    id          int          NOT NULL AUTO_INCREMENT,
    name        varchar(255) NOT NULL,
    heads       varchar(255) NULL,
    area        varchar(255) NULL,
    is_active   boolean   default true,
    user_id     int          NOT NULL,
    created_by  int          NOT NULL,
    modified_by int          NOT NULL,
    created_at  timestamp default CURRENT_TIMESTAMP,
    modified_at timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

create table if not exists tblm_attendance
(
    id                         int NOT NULL AUTO_INCREMENT,
    attendance_date            timestamp default CURRENT_TIMESTAMP,
    production                 int,
    dhaga                      int,
    frames                     int,
    attendance_user_image_size int NULL,
    attendance_user_image_name varchar(255) NULL,
    attendance_user_image_path varchar(255) NULL,
    user_id                    int NOT NULL,
    machine_id                 int NOT NULL,
    shift_id                   int NOT NULL,
    salary_type_id             int NOT NULL,
    is_active                  boolean   default true,
    created_by                 int NOT NULL,
    modified_by                int NOT NULL,
    created_at                 timestamp default CURRENT_TIMESTAMP,
    modified_at                timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_user_attendance FOREIGN KEY (user_id) REFERENCES tblm_user (id),
    CONSTRAINT fk_machine_attendance FOREIGN KEY (machine_id) REFERENCES tblm_machine (id),
    CONSTRAINT fk_shift_attendance FOREIGN KEY (shift_id) REFERENCES tbls_shift (id),
    CONSTRAINT fk_salary_type_attendance FOREIGN KEY (salary_type_id) REFERENCES tbls_salary_type (id)
);

-- mapping tables
create table if not exists tblt_user_role_mapping
(
    id          int NOT NULL AUTO_INCREMENT,
    user_id     int NOT NULL,
    role_id     int NOT NULL,
    created_at  timestamp default CURRENT_TIMESTAMP,
    modified_at timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_user_user_role_mapping FOREIGN KEY (user_id) REFERENCES tblm_user (id),
    CONSTRAINT fk_role_user_role_mapping FOREIGN KEY (role_id) REFERENCES tbls_role (id)
);

create table if not exists tblt_user_salary_mapping
(
    id             int NOT NULL AUTO_INCREMENT,
    user_id        int NOT NULL,
    salary         int NOT NULL,
    salary_type_id int NOT NULL,
    salary_date    timestamp default CURRENT_TIMESTAMP,
    is_active      boolean   default true,
    created_by     int NOT NULL,
    modified_by    int NOT NULL,
    created_at     timestamp default CURRENT_TIMESTAMP,
    modified_at    timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_user_user_salary_mapping FOREIGN KEY (user_id) REFERENCES tblm_user (id),
    CONSTRAINT fk_salary_user_salary_mapping FOREIGN KEY (salary_type_id) REFERENCES tbls_salary_type (id)
);

create table if not exists tblt_user_advance_salary_mapping
(
    id                  int NOT NULL AUTO_INCREMENT,
    user_id             int NOT NULL,
    advance_salary      int NOT NULL,
    advance_salary_date timestamp default CURRENT_TIMESTAMP,
    paid_by_user_id     int NOT NULL,
    created_at          timestamp default CURRENT_TIMESTAMP,
    modified_at         timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_user_user_advance_salary_mapping FOREIGN KEY (user_id) REFERENCES tblm_user (id),
    CONSTRAINT fk_paid_by_user_user_advance_salary_mapping FOREIGN KEY (paid_by_user_id) REFERENCES tblm_user (id)
);

create table if not exists tblt_user_payment_mapping
(
    id              int NOT NULL AUTO_INCREMENT,
    user_id         int NOT NULL,
    payment_amount  int NOT NULL,
    advance_payment int NOT NULL,
    working_days    int NOT NULL,
    payment_date    timestamp default CURRENT_TIMESTAMP,
    is_active int not null,
    salary_type_id int NOT NULL,
    created_by      int NOT NULL,
    modified_by     int NOT NULL,
    created_at      timestamp default CURRENT_TIMESTAMP,
    modified_at     timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_user_user_payment_mapping FOREIGN KEY (user_id) REFERENCES tblm_user (id),
    CONSTRAINT fk_salary_type_user_payment_mapping FOREIGN KEY (salary_type_id) REFERENCES tbls_salary_type (id)
);

-- audit tables
create table if not exists tbla_user
(
    id                  int          NOT NULL AUTO_INCREMENT,
    name                varchar(255) NOT NULL,
    mobile_number       varchar(255) NULL,
    email               varchar(255) NULL,
    is_active           boolean   default true,
    manager_id          int          NOT NULL,
    user_id             int          NOT NULL,
    created_by_user_id  int          NOT NULL,
    modified_by_user_id int          NOT NULL,
    created_by          int          NOT NULL,
    modified_by         int          NOT NULL,
    created_at          timestamp default CURRENT_TIMESTAMP,
    modified_at         timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_audit_user FOREIGN KEY (user_id) REFERENCES tblm_user (id)
);

create table if not exists tbla_user_salary_mapping
(
    id             int NOT NULL AUTO_INCREMENT,
    user_id        int NOT NULL,
    salary_id      int NOT NULL,
    salary         int NOT NULL,
    salary_type_id int NOT NULL,
    salary_date    timestamp default CURRENT_TIMESTAMP,
    created_by     int NOT NULL,
    modified_by    int NOT NULL,
    created_at     timestamp default CURRENT_TIMESTAMP,
    modified_at    timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_audit_salary_user_salary_mapping FOREIGN KEY (salary_id) REFERENCES tblt_user_salary_mapping (id),
    CONSTRAINT fk_audit_user_user_salary_mapping FOREIGN KEY (user_id) REFERENCES tblm_user (id),
    CONSTRAINT fk_audit_salary_type_user_salary_mapping FOREIGN KEY (salary_type_id) REFERENCES tbls_salary_type (id)
);