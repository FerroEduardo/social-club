SET TIMEZONE TO 'UTC';

DROP SCHEMA IF EXISTS social CASCADE;
CREATE SCHEMA IF NOT EXISTS social;

DROP TABLE IF EXISTS social.user CASCADE;
CREATE TABLE IF NOT EXISTS social.user
(
    id        SERIAL PRIMARY KEY,
    name      VARCHAR(100)        NOT NULL,
    email     VARCHAR(300) UNIQUE NOT NULL,
    image_url VARCHAR(300)        NOT NULL
);

DROP TABLE IF EXISTS social.game CASCADE;
CREATE TABLE IF NOT EXISTS social.game
(
    id     SERIAL PRIMARY KEY,
    name   VARCHAR(100) NOT NULL,
    studio VARCHAR(100) NOT NULL
);

DROP TABLE IF EXISTS social.image_type CASCADE;
CREATE TABLE IF NOT EXISTS social.image_type
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(10) NOT NULL
);

INSERT INTO social.image_type (name)
VALUES ('BLOB'),
       ('S3'),
       ('LOCAL');

DROP TABLE IF EXISTS social.image CASCADE;
CREATE TABLE IF NOT EXISTS social.image
(
    id    SERIAL PRIMARY KEY,
    blob  BYTEA   NULL,
    s3    VARCHAR NULL,
    local VARCHAR NULL,
    -- type  INTEGER NOT NULL REFERENCES social.image_type (id) ON DELETE CASCADE,
    CONSTRAINT check_value CHECK (
        (
                    (blob IS NOT NULL) AND (s3 IS NULL) AND (local IS NULL) OR
                    ((blob IS NULL) AND (s3 IS NOT NULL) AND (local IS NULL)) OR
                    ((blob IS NULL) AND (s3 IS NULL) AND (local IS NOT NULL))
            )
        ) -- Insert blob or s3, not both or none
);

DROP TABLE IF EXISTS social.post CASCADE;
CREATE TABLE IF NOT EXISTS social.post
(
    id          SERIAL PRIMARY KEY,
    author_id   INTEGER      NOT NULL REFERENCES social.user (id) ON DELETE CASCADE,
    game_id     INTEGER      NOT NULL REFERENCES social.game (id) ON DELETE CASCADE,
    description VARCHAR(200) NULL,
    reputation  INTEGER      NOT NULL DEFAULT 0,
    image_id    INTEGER      NOT NULL REFERENCES social.image (id) ON DELETE CASCADE,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT (current_timestamp AT TIME ZONE 'UTC'),
    modified_at TIMESTAMPTZ  NOT NULL DEFAULT (current_timestamp AT TIME ZONE 'UTC')
);

DROP TABLE IF EXISTS social.post_mention CASCADE;
CREATE TABLE IF NOT EXISTS social.post_mention
(
    mentioned_user_id INTEGER NOT NULL REFERENCES social.user (id) ON DELETE CASCADE,
    post_id           INTEGER NOT NULL REFERENCES social.post (id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS social.post_like CASCADE;
CREATE TABLE IF NOT EXISTS social.post_like
(
    user_id INTEGER NOT NULL REFERENCES social.user (id) ON DELETE CASCADE,
    post_id INTEGER NOT NULL REFERENCES social.post (id) ON DELETE CASCADE,
    value   BOOL    NOT NULL
);

DROP TABLE IF EXISTS social.post_comment CASCADE;
CREATE TABLE IF NOT EXISTS social.post_comment
(
    id             SERIAL PRIMARY KEY,
    author_id      INTEGER NOT NULL REFERENCES social.user (id) ON DELETE CASCADE,
    post_id        INTEGER NOT NULL REFERENCES social.post (id) ON DELETE CASCADE,
    post_parent_id INTEGER NULL REFERENCES social.post_comment (id) ON DELETE CASCADE,
    value          BOOL    NOT NULL
);