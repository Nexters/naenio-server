CREATE TABLE IF NOT EXISTS member
(
    id                      BIGINT       NOT NULL AUTO_INCREMENT,
    auth_id                 varchar(255) NOT NULL,
    auth_service_type       varchar(255) NOT NULL,
    nickname                varchar(255) UNIQUE,
    created_date_time       DATETIME     NOT NULL,
    last_modified_date_time DATETIME     NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS post
(
    id                      BIGINT       NOT NULL AUTO_INCREMENT,
    member_id               BIGINT       NOT NULL,
    title                   varchar(255) NOT NULL,
    content                 varchar(255) NOT NULL,
    category_id             BIGINT       NOT NULL,
    created_date_time       DATETIME     NOT NULL,
    last_modified_date_time DATETIME     NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS choice
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    post_id  BIGINT       NOT NULL,
    sequence BIGINT       NOT NULL,
    name     varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS category
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    PRIMARY KEY (id)
);