CREATE TABLE IF NOT EXISTS member
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    oauth_id varchar(255) NOT NULL UNIQUE,
    nickname varchar(255) UNIQUE,
    PRIMARY KEY (id)
);
