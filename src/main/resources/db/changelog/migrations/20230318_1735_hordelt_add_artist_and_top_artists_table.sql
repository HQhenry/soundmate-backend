-- liquibase formatted sql

-- changeset liquibase:20230305_2012_hordelt_setup_account_table.sql
create table artist
(
    artist_id bigserial
        primary key,
    name      text,
    image_url text
);

create table top_artists
(
    profile_fk bigint
        constraint top_artists_profile_profile_id_fk
            references public.profile,
    artist_fk  bigint
        constraint top_artists_artist_artist_id_fk
            references public.artist
);