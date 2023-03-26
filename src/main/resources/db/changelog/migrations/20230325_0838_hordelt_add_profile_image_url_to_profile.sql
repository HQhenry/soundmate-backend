-- liquibase formatted sql

-- changeset liquibase:20230325_0838_hordelt_add_profile_image_url_to_profile.sql
alter table profile
    add column profile_image_url text;