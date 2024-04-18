create table post
(
    uuid     uuid    not null,
    author_uuid uuid    not null,
    text text not null,
    edit_dt timestamp not null,

    CONSTRAINT pk_post PRIMARY KEY (uuid)
);
