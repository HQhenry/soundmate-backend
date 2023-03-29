-- liquibase formatted sql

-- changeset liquibase:20230329_1902_proehr_persist_match_distance.sql
alter table public.match
    add distance double precision default 1000000;
