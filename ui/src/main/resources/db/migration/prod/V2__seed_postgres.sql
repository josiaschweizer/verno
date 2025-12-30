INSERT INTO gender (created_at, name, description)
SELECT CURRENT_TIMESTAMP, 'Male', 'Männlich'
    WHERE NOT EXISTS (SELECT 1 FROM gender WHERE name = 'Male');

INSERT INTO gender (created_at, name, description)
SELECT CURRENT_TIMESTAMP, 'Female', 'Weiblich'
    WHERE NOT EXISTS (SELECT 1 FROM gender WHERE name = 'Female');

INSERT INTO mandant_settings (course_weeks_per_schedule, max_participants_per_course, enforce_quantity_settings)
SELECT 8, 12, TRUE
    WHERE NOT EXISTS (SELECT 1 FROM mandant_settings);