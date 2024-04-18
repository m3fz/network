create table users
(
    uuid     uuid    not null,
    username varchar not null,
    password_hash varchar,
    enabled  boolean not null default true,

    firstname varchar,
    lastname varchar,
    age int,
    gender varchar,
    interests varchar,
    city varchar,

    CONSTRAINT pk_users PRIMARY KEY (uuid)
);

CREATE UNIQUE INDEX users_username_idx ON users (username);