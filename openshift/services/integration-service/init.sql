create schema if not exists public;

comment on schema public
is 'Integrated database schema';

create sequence if not exists seq_customer_id;

create sequence if not exists seq_order_id;

create table if not exists customer
(
  id          bigint       not null
    constraint customer_pkey
    primary key
    constraint customer_id_check
    check (id >= 0),
  created_at  timestamp    not null,
  email       varchar(100) not null,
  full_name   varchar(255) not null,
  modified_at timestamp    not null,
  version     bigint       not null
);

create table if not exists customer_order
(
  id           bigint       not null
    constraint customer_order_pkey
    primary key
    constraint customer_order_id_check
    check (id >= 0),
  created_at   timestamp    not null,
  data         varchar(255) not null,
  delivered_at timestamp    not null,
  modified_at  timestamp    not null,
  version      bigint,
  customer_id  bigint
    constraint fkf9abd30bhiqvugayxlpq8ryq9
    references customer
);

commit;
