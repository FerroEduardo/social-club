SET TIMEZONE TO 'UTC';

DROP SCHEMA IF EXISTS social CASCADE;
CREATE SCHEMA IF NOT EXISTS social;

DROP TABLE IF EXISTS social.game CASCADE;
CREATE TABLE IF NOT EXISTS social.game
(
    id     SERIAL PRIMARY KEY,
    name   VARCHAR(100) NOT NULL,
    studio VARCHAR(100) NOT NULL,
    image_url VARCHAR(150) NOT NULL
);
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
        ), -- Insert blob or s3, not both or none
    extension VARCHAR(50) NOT NULL,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT (CURRENT_TIMESTAMP),
    deleted_at  TIMESTAMPTZ  NULL
);

DROP TABLE IF EXISTS social.user CASCADE;
CREATE TABLE IF NOT EXISTS social.user
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(100)        NOT NULL,
    email    VARCHAR(300) UNIQUE NOT NULL,
    image_id INTEGER             NOT NULL REFERENCES social.image (id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS social.post CASCADE;
CREATE TABLE IF NOT EXISTS social.post
(
    id          SERIAL PRIMARY KEY,
    author_id   INTEGER      NOT NULL REFERENCES social.user (id) ON DELETE CASCADE,
    game_id     INTEGER      NOT NULL REFERENCES social.game (id) ON DELETE CASCADE,
    title       VARCHAR(100) NULL,
    description VARCHAR(200) NULL,
    image_id    INTEGER      NOT NULL REFERENCES social.image (id) ON DELETE CASCADE,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT (CURRENT_TIMESTAMP),
    modified_at TIMESTAMPTZ  NOT NULL DEFAULT (CURRENT_TIMESTAMP),
    deleted_at  TIMESTAMPTZ  NULL
);

DROP TABLE IF EXISTS social.post_vote CASCADE;
CREATE TABLE IF NOT EXISTS social.post_vote
(
    user_id     INTEGER     NOT NULL REFERENCES social.user (id) ON DELETE CASCADE,
    post_id     INTEGER     NOT NULL REFERENCES social.post (id) ON DELETE CASCADE,
    value       SMALLINT    NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT (CURRENT_TIMESTAMP),
    modified_at TIMESTAMPTZ NOT NULL DEFAULT (CURRENT_TIMESTAMP),
    PRIMARY KEY (user_id, post_id)
);
DROP INDEX IF EXISTS idx_post_id;
CREATE INDEX IF NOT EXISTS idx_post_id ON social.post_vote (post_id);

DROP VIEW IF EXISTS social.post_reputation CASCADE;
CREATE OR REPLACE VIEW social.post_reputation AS
SELECT pv.post_id, SUM(pv.value) AS reputation
FROM social.post_vote pv
GROUP BY pv.post_id;

DROP TABLE IF EXISTS social.post_comment CASCADE;
CREATE TABLE IF NOT EXISTS social.post_comment
(
    id          SERIAL PRIMARY KEY,
    author_id   INTEGER      NOT NULL REFERENCES social.user (id) ON DELETE CASCADE,
    post_id     INTEGER      NOT NULL REFERENCES social.post (id) ON DELETE CASCADE,
    value       VARCHAR(200) NOT NULL,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT (CURRENT_TIMESTAMP),
    modified_at TIMESTAMPTZ  NOT NULL DEFAULT (CURRENT_TIMESTAMP),
    deleted_at  TIMESTAMPTZ  NULL
);
DROP INDEX IF EXISTS idx_post_id;
CREATE INDEX IF NOT EXISTS idx_post_id ON social.post_comment (post_id);
