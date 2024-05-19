create table counter_tx_log
(
    tx_uuid     uuid      not null,
    count       int       not null,
    rolled_back boolean   not null default false,
    tx_dt       timestamp not null default now(),

    CONSTRAINT pk_counter_tx_log PRIMARY KEY (tx_uuid)
);
