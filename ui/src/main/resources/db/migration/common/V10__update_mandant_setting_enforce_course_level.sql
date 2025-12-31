ALTER TABLE mandant_settings
ADD COLUMN IF NOT EXISTS enforce_course_level_settings BOOLEAN NOT NULL DEFAULT FALSE;