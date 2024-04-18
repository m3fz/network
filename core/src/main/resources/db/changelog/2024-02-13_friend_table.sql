create table friend
(
    user_uuid   uuid not null,
    friend_uuid uuid not null,

    CONSTRAINT pk_friend PRIMARY KEY (user_uuid, friend_uuid),
    CONSTRAINT fk_friend_user FOREIGN KEY (user_uuid) REFERENCES users(uuid),
    CONSTRAINT fk_friend_friend FOREIGN KEY (friend_uuid) REFERENCES users(uuid)
);
