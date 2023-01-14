drop table if exists public.account cascade;
drop table if exists public.category cascade;
drop table if exists public.order cascade;
drop table if exists public.order_item cascade;
drop table if exists public.producer cascade;
drop table if exists public.product cascade;

create table public.category
(
    id            integer primary key not null,
    name          varchar(60)         not null,
    url           varchar(60) unique  not null,
    product_count integer default 0   not null
);

create table public.producer
(
    id            integer primary key not null,
    name          varchar(60)         not null,
    product_count integer default 0   not null
);

create table public.product
(
    id          integer primary key not null,
    name        varchar(255)        not null,
    price       integer       not null,
    description text                not null,
    image_link  varchar(255)        not null,
    id_category integer             not null,
    id_producer integer             not null,
    foreign key (id_category) references public.category (id)
        on delete restrict on update cascade,
    foreign key (id_producer) references public.producer (id)
        on delete restrict on update cascade
);

create table public.account
(
    id    integer primary key not null,
    name  varchar(60)         not null,
    email varchar(100) unique not null
);

alter table account
    add avatar_url varchar(255);

create table public.order
(
    id              bigint primary key not null,
    date_of_created timestamp default now(),
    id_account      integer            not null,
    foreign key (id_account) references public.account (id)
        on delete restrict on update cascade
);

create table public.order_item
(
    id         bigint primary key not null,
    id_order   bigint             not null,
    id_product integer            not null,
    count      integer            not null,
    foreign key (id_order) references public.order (id)
        on delete cascade on update cascade,
    foreign key (id_product) references public.product (id)
        on delete restrict on update cascade
);

-- SEQUENCE: public.account_seq

-- DROP SEQUENCE IF EXISTS public.account_seq;

CREATE SEQUENCE IF NOT EXISTS public.account_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.account_seq
    OWNER TO ishop;

-- SEQUENCE: public.order_seq

-- DROP SEQUENCE IF EXISTS public.order_seq;

CREATE SEQUENCE IF NOT EXISTS public.order_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.order_seq
    OWNER TO ishop;

-- SEQUENCE: public.order_item_seq

-- DROP SEQUENCE IF EXISTS public.order_item_seq;

CREATE SEQUENCE IF NOT EXISTS public.order_item_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.order_item_seq
    OWNER TO ishop;