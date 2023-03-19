-- liquibase formatted sql

-- changeset liquibase:20230305_2012_hordelt_setup_account_table.sql
create table account
(
    account_id    bigserial
        constraint account_pk
            primary key,
    access_token text,
    refresh_Token text,
    access_expires_on timestamp,
    password_hash text,
    password_salt text,
    created_at    timestamp
);

create table profile
(
    profile_id        bigserial
        constraint profile_pk
            primary key,
    name              text,
    info_text         text,
    account_fk        bigint
        constraint profile_account_account_id_fk
            references public.account,
    spotify_user_id   text,
    age               integer,
    contact_info      text,
    novel_factor      double precision,
    mainstream_factor double precision,
    diverse_factor    double precision
);

create type listening_type as enum ('NOVEL', 'MAINSTREAM', 'DIVERSE');

create table public.match
(
    match_id        bigserial
        primary key,
    profile_1_fk    bigint
        constraint match_profile_profile_id_fk
            references public.profile,
    profile_2_fk    bigint
        constraint match_profile_profile_id_fk2
            references public.profile,
    date            timestamp,
    matched_on_type listening_type
);
