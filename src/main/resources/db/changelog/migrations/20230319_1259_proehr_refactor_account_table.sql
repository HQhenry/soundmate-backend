-- liquibase formatted sql

-- changeset liquibase:20230319_1259_proehr_refactor_account_table.sql
alter table account
    rename column password_hash to password;

alter table account
    drop column password_salt;

alter table account
    add username text;
