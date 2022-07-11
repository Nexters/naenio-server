CREATE TABLE IF NOT EXISTS member
(
    id                BIGINT       NOT NULL AUTO_INCREMENT,
    auth_id           varchar(255) NOT NULL,
    auth_service_type varchar(255) NOT NULL,
    nickname          varchar(255) UNIQUE,
    PRIMARY KEY (id)
);