CREATE TABLE IF NOT EXISTS member
(
    id                      BIGINT       NOT NULL AUTO_INCREMENT,
    auth_id                 varchar(255) NOT NULL,
    auth_service_type       varchar(255) NOT NULL,
    nickname                varchar(255) UNIQUE,
    profile_image_index     BIGINT       NULL,
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
    created_date_time       DATETIME     NOT NULL,
    last_modified_date_time DATETIME     NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS choice
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    post_id  BIGINT       NOT NULL,
    sequence BIGINT       NOT NULL,
    name     VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS category
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS vote
(
    id                      BIGINT   NOT NULL AUTO_INCREMENT,
    member_id               BIGINT   NOT NULL,
    post_id                 BIGINT   NOT NULL,
    choice_id               BIGINT   NOT NULL,
    created_date_time       DATETIME NOT NULL,
    last_modified_date_time DATETIME NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS comment
(
    id                      BIGINT       NOT NULL AUTO_INCREMENT,
    member_id               BIGINT       NOT NULL,
    parent_id               BIGINT       NOT NULL,
    parent_type             VARCHAR(20)  NOT NULL,
    content                 VARCHAR(255) NOT NULL,
    created_date_time       DATETIME     NOT NULL,
    last_modified_date_time DATETIME     NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS comment_like
(
    id                      BIGINT   NOT NULL AUTO_INCREMENT,
    comment_id              BIGINT   NOT NULL,
    member_id               BIGINT   NOT NULL,
    created_date_time       DATETIME NOT NULL,
    last_modified_date_time DATETIME NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS post_choice_vote_count
(
    id                  BIGINT NOT NULL AUTO_INCREMENT,
    post_id             BIGINT NOT NULL,
    first_choice_count  BIGINT NOT NULL,
    second_choice_count BIGINT NOT NULL,
    total_count         BIGINT NOT NULL,
    rate                DOUBLE NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS post_comment_count
(
    id            BIGINT NOT NULL AUTO_INCREMENT,
    post_id       BIGINT NOT NULL,
    comment_count BIGINT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS post_vote_by_day
(
    id             BIGINT NOT NULL AUTO_INCREMENT,
    post_id        BIGINT NOT NULL,
    aggregate_date DATE   NOT NULL,
    count          BIGINT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS post_vote_count
(
    id      BIGINT NOT NULL AUTO_INCREMENT,
    post_id BIGINT NOT NULL,
    count   BIGINT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS notice
(
    id      BIGINT       NOT NULL AUTO_INCREMENT,
    title   VARCHAR(255) NOT NULL,
    content VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);