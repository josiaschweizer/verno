-- Vxx__add_color_to_course_schedule.sql

ALTER TABLE course_schedule
    ADD COLUMN color VARCHAR(16);

UPDATE course_schedule
SET color = '#55A6F6'
WHERE color IS NULL;

ALTER TABLE course_schedule
    ALTER COLUMN color SET DEFAULT '#55A6F6';

ALTER TABLE course_schedule
    ALTER COLUMN color SET NOT NULL;