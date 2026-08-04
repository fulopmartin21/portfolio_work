ALTER TABLE users
    ADD COLUMN token_version BIGINT NOT NULL DEFAULT 0;

ALTER TABLE users
    ADD COLUMN created_at TIMESTAMP WITH TIME ZONE;

ALTER TABLE users
    ADD COLUMN created_by VARCHAR(100);

ALTER TABLE users
    ADD COLUMN updated_at TIMESTAMP WITH TIME ZONE;

ALTER TABLE users
    ADD COLUMN updated_by VARCHAR(100);

UPDATE users
SET created_at = CURRENT_TIMESTAMP,
    created_by = 'SYSTEM',
    updated_at = CURRENT_TIMESTAMP,
    updated_by = 'SYSTEM';

ALTER TABLE users
    ALTER COLUMN created_at SET NOT NULL;

ALTER TABLE users
    ALTER COLUMN created_by SET NOT NULL;

ALTER TABLE users
    ALTER COLUMN updated_at SET NOT NULL;

ALTER TABLE users
    ALTER COLUMN updated_by SET NOT NULL;