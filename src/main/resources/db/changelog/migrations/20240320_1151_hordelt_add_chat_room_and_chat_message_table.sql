-- liquibase formatted sql

-- changeset liquibase:20240320_1151_hordelt_add_chat_room_and_chat_message_table.sql

create table public.chat_room
(
    chat_room_id        bigserial
        primary key,
    chat_id text,
    sender_fk    bigint
        constraint chat_room_profile_profile_id_fk
            references public.profile,
    recipient_fk   bigint
        constraint chat_room_profile_profile_id_fk2
            references public.profile


);

create table public.chat_message
(
    chat_message_id        bigserial
        primary key,
    chat_id text,
    sender_fk    bigint
        constraint chat_room_profile_profile_id_fk
            references public.profile,
    recipient_fk   bigint
        constraint chat_room_profile_profile_id_fk2
            references public.profile,
    content text,
    timestamp timestamp



);