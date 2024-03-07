-- liquibase formatted sql

-- changeset liquibase:20240307_1018_hordelt_add_gender_to_profile.sql

CREATE TYPE gender_type as enum ('FEMALE', 'MALE', 'DIVERSE');

alter table profile
    add column gender_type text ;
