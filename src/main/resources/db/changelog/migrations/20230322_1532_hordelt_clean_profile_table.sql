-- liquibase formatted sql

-- changeset liquibase:20230322_1532_hordelt_clean_profile_table.sql
alter table profile
    drop column spotify_user_id;

alter table profile
    drop column info_text;
