-- liquibase formatted sql

-- changeset liquibase:20240331_0738_hordelt_add_latitude_longitude_to_profile.sql

alter table public.profile
    add latitude double precision;

alter table public.profile
    add longitude double precision;