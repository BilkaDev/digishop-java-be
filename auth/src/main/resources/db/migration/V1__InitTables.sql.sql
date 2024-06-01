CREATE TABLE "users"
(
    id        serial primary key,
    uuid      varchar not null,
    login     varchar not null unique,
    email     varchar not null unique,
    password  varchar not null,
    role      varchar not null,
    isLock    boolean DEFAULT true,
    isEnabled boolean DEFAULT false
);