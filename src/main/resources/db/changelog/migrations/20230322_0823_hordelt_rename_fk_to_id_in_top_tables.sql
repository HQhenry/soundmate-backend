-- liquibase formatted sql

-- changeset liquibase:20230322_0823_hordelt_rename_fk_to_id_in_top_tables.sql
alter table top_artists
    rename column profile_fk to profile_id;
alter table top_artists
    rename column artist_fk to artist_id;

alter table top_genres
    rename column profile_fk to profile_id;

alter table top_genres
    rename column genre_fk to genre_id;