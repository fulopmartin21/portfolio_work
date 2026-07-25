CREATE TABLE users
(
    id         UUID         NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(60)  NOT NULL,

    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE UNIQUE INDEX uk_users_email_lower
    ON users (LOWER(email));