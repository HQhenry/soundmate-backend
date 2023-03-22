-- liquibase formatted sql

-- changeset liquibase:20230321_0901_hordelt_add_genre_and_top_genres_table.sql
create table genre
(
    genre_id bigserial
        primary key,
    name      text
);

create table top_genres
(
    profile_fk bigint
        constraint top_genres_profile_profile_id_fk
            references public.profile,
    genre_fk  bigint
        constraint top_genres_genre_genre_id_fk
            references public.genre
);