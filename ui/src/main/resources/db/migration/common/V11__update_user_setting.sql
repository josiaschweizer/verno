ALTER TABLE app_user_settings
    ADD COLUMN user_id BIGINT;

ALTER TABLE app_user_settings
    ADD COLUMN theme VARCHAR(50);

ALTER TABLE app_user_settings
    ALTER COLUMN user_id SET NOT NULL;

ALTER TABLE app_user_settings
    ADD CONSTRAINT fk_app_user_settings_user
        FOREIGN KEY (user_id)
            REFERENCES app_user (id)
            ON DELETE CASCADE;

ALTER TABLE app_user_settings
    ADD CONSTRAINT uq_app_user_settings_user
        UNIQUE (user_id);