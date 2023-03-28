-- liquibase formatted sql

-- changeset liquibase:20230327_2027_hordelt_change_changed_on_type_to_string.sql
alter table match
alter column matched_on_type type text;