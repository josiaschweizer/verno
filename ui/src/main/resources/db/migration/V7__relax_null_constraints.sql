ALTER TABLE gender ALTER COLUMN name DROP NOT NULL;

ALTER TABLE address ALTER COLUMN street DROP NOT NULL;
ALTER TABLE address ALTER COLUMN house_number DROP NOT NULL;
ALTER TABLE address ALTER COLUMN zip_code DROP NOT NULL;
ALTER TABLE address ALTER COLUMN city DROP NOT NULL;
ALTER TABLE address ALTER COLUMN country DROP NOT NULL;

ALTER TABLE course_level ALTER COLUMN code DROP NOT NULL;
ALTER TABLE course_level ALTER COLUMN name DROP NOT NULL;
ALTER TABLE course_level ALTER COLUMN sorting_order DROP NOT NULL;

ALTER TABLE course_schedule ALTER COLUMN title DROP NOT NULL;

ALTER TABLE course_schedule_week ALTER COLUMN course_schedule_id DROP NOT NULL;
ALTER TABLE course_schedule_week ALTER COLUMN sort_index DROP NOT NULL;
ALTER TABLE course_schedule_week ALTER COLUMN week DROP NOT NULL;

ALTER TABLE instructor ALTER COLUMN firstname DROP NOT NULL;
ALTER TABLE instructor ALTER COLUMN lastname DROP NOT NULL;
ALTER TABLE instructor ALTER COLUMN email DROP NOT NULL;

ALTER TABLE parent ALTER COLUMN firstname DROP NOT NULL;
ALTER TABLE parent ALTER COLUMN lastname DROP NOT NULL;
ALTER TABLE parent ALTER COLUMN email DROP NOT NULL;

ALTER TABLE course ALTER COLUMN title DROP NOT NULL;
ALTER TABLE course ALTER COLUMN capacity DROP NOT NULL;

ALTER TABLE course_weekday ALTER COLUMN weekday DROP NOT NULL;

ALTER TABLE participant ALTER COLUMN firstname DROP NOT NULL;
ALTER TABLE participant ALTER COLUMN lastname DROP NOT NULL;
ALTER TABLE participant ALTER COLUMN email DROP NOT NULL;

ALTER TABLE mandant_settings ALTER COLUMN course_weeks_per_schedule DROP NOT NULL;
ALTER TABLE mandant_settings ALTER COLUMN max_participants_per_course DROP NOT NULL;
ALTER TABLE mandant_settings ALTER COLUMN enforce_quantity_settings DROP NOT NULL;

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