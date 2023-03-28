-- liquibase formatted sql

-- changeset liquibase:20230328_1901_hordelt_add_bio_to_profile.sql
alter table public.profile
    add bio text default '';