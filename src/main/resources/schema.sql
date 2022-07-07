CREATE TABLE member
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    oauth_id varchar(255) NOT NULL,
    nickname varchar(255),
    PRIMARY KEY (id)
);

create index idx_member_key on member (oauth_id);

