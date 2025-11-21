ALTER TABLE users
    ADD COLUMN is_active BOOLEAN DEFAULT FALSE,
    ADD COLUMN verification_code VARCHAR(6);