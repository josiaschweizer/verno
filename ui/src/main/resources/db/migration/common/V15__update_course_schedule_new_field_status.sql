ALTER TABLE course_schedule
    ADD COLUMN IF NOT EXISTS status VARCHAR(16);

UPDATE course_schedule
SET status = 'PLANNED'
WHERE status IS NULL;

ALTER TABLE course_schedule
    ALTER COLUMN status SET DEFAULT 'PLANNED';

ALTER TABLE course_schedule
    ALTER COLUMN status SET NOT NULL;

ALTER TABLE course_schedule
    ADD CONSTRAINT chk_course_schedule_status
        CHECK (status IN ('PLANNED', 'ACTIVE', 'COMPLETED'));