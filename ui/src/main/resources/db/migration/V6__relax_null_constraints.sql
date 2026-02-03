ALTER TABLE parent
    ALTER COLUMN email DROP NOT NULL;

ALTER TABLE instructor
    ALTER COLUMN email SET NOT NULL;
ALTER TABLE participant
    ALTER COLUMN email SET NOT NULL;

ALTER TABLE parent
    ALTER COLUMN firstname SET NOT NULL;
ALTER TABLE parent
    ALTER COLUMN lastname SET NOT NULL;

ALTER TABLE instructor
    ALTER COLUMN firstname SET NOT NULL;
ALTER TABLE instructor
    ALTER COLUMN lastname SET NOT NULL;

ALTER TABLE participant
    ALTER COLUMN firstname SET NOT NULL;
ALTER TABLE participant
    ALTER COLUMN lastname SET NOT NULL;

ALTER TABLE course
    ALTER COLUMN title SET NOT NULL;
ALTER TABLE course_schedule
    ALTER COLUMN title SET NOT NULL;
ALTER TABLE course_level
    ALTER COLUMN code SET NOT NULL;
ALTER TABLE gender
    ALTER COLUMN name SET NOT NULL;

UPDATE parent
SET email = NULL
WHERE email IS NOT NULL
  AND trim(email) = '';

UPDATE instructor
SET phone = NULL
WHERE phone IS NOT NULL
  AND trim(phone) = '';

UPDATE participant
SET phone = NULL
WHERE phone IS NOT NULL
  AND trim(phone) = '';