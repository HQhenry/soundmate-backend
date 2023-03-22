-- liquibase formatted sql

-- changeset liquibase:20230322_1532_hordelt_clean_profile_table.sql
alter table top_artists
    rename column profile_fk to profile_id;
alter table top_artists
    rename column artist_fk to artist_id;

alter table top_genres
    rename column profile_fk to profile_id;

alter table top_genres
    rename column genre_fk to genre_id;

alter table profile
    drop column spotify_user_id;

alter table profile
    drop column info_text;