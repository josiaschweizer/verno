ALTER TABLE app_user_settings
    ADD COLUMN IF NOT EXISTS language_tag VARCHAR (16);

UPDATE app_user_settings
SET language_tag = 'de'
WHERE language_tag IS NULL
   OR TRIM(language_tag) = '';

ALTER TABLE app_user_settings
    ALTER COLUMN language_tag SET NOT NULL;

ALTER TABLE app_user_settings
    ALTER COLUMN language_tag SET DEFAULT 'de';