create table dialog_message
(
    uuid              uuid      not null,
    sender            uuid      not null,
    receiver          uuid      not null,
    text              text      not null,
    create_dt         timestamp not null,
    distribution_hash uuid      not null,

    CONSTRAINT pk_dialog_message PRIMARY KEY (uuid, distribution_hash)
);
