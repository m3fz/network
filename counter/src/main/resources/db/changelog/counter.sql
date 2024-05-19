create table counter
(
    user_uuid    uuid not null,
    unread_count int  not null,
    last_tx      uuid not null,

    CONSTRAINT pk_counter PRIMARY KEY (user_uuid),
    CONSTRAINT fk_counter_last_tx FOREIGN KEY (last_tx) REFERENCES counter_tx_log (tx_uuid)
);
