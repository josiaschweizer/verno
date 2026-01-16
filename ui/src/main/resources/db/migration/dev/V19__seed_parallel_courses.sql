-- V19__seed_parallel_courses.sql
-- H2-kompatibel (ohne WITH + INSERT/RETURNING)
-- Idee: IDs immer über eindeutige Titles wiederholen (Subselect), keine Variablen nötig.

-- 1) Cleanup (falls das Script in einer frischen Dev-DB doch schon mal gelaufen ist)
DELETE FROM course_weekday
WHERE course_id IN (
    SELECT id FROM course
    WHERE title IN (
                    'Seed: Java Grundlagen A (parallel)',
                    'Seed: Java Grundlagen B (parallel)'
        )
);

DELETE FROM course
WHERE title IN (
                'Seed: Java Grundlagen A (parallel)',
                'Seed: Java Grundlagen B (parallel)'
    );

DELETE FROM course_schedule_week
WHERE course_schedule_id IN (
    SELECT id FROM course_schedule
    WHERE title = 'Seed: Parallelkurse (clean)'
);

DELETE FROM course_schedule
WHERE title = 'Seed: Parallelkurse (clean)';

-- 2) CourseSchedule anlegen
INSERT INTO course_schedule (created_at, title, status)
VALUES (CURRENT_TIMESTAMP, 'Seed: Parallelkurse (clean)', 'PLANNED');

-- 3) Wochen zur Schedule hinzufügen
INSERT INTO course_schedule_week (course_schedule_id, week, sort_index)
SELECT cs.id, w.week, w.sort_index
FROM course_schedule cs
         JOIN (
    SELECT '2026-W10' AS week, 0 AS sort_index
    UNION ALL SELECT '2026-W11', 1
    UNION ALL SELECT '2026-W12', 2
) w ON 1=1
WHERE cs.title = 'Seed: Parallelkurse (clean)';

-- 4) Zwei parallele Kurse anlegen (beide referenzieren die neue Schedule)
INSERT INTO course (
    created_at,
    title,
    capacity,
    location,
    course_schedule_id,
    start_time,
    end_time,
    note
)
SELECT
            CURRENT_TIMESTAMP,
            'Seed: Java Grundlagen A (parallel)',
            16,
            'Raum 101',
            cs.id,
            TIME '08:30:00',
            TIME '10:00:00',
            'Seed-Kurs A'
FROM course_schedule cs
WHERE cs.title = 'Seed: Parallelkurse (clean)';

INSERT INTO course (
    created_at,
    title,
    capacity,
    location,
    course_schedule_id,
    start_time,
    end_time,
    note
)
SELECT
            CURRENT_TIMESTAMP,
            'Seed: Java Grundlagen B (parallel)',
            16,
            'Raum 102',
            cs.id,
            TIME '08:30:00',
            TIME '10:00:00',
            'Seed-Kurs B'
FROM course_schedule cs
WHERE cs.title = 'Seed: Parallelkurse (clean)';

-- 5) Weekdays für beide Kurse setzen
INSERT INTO course_weekday (course_id, weekday, sort_index)
SELECT c.id, d.weekday, d.sort_index
FROM course c
         JOIN (
    SELECT 'MONDAY' AS weekday, 0 AS sort_index
    UNION ALL SELECT 'WEDNESDAY', 1
) d ON 1=1
WHERE c.title = 'Seed: Java Grundlagen A (parallel)';

INSERT INTO course_weekday (course_id, weekday, sort_index)
SELECT c.id, d.weekday, d.sort_index
FROM course c
         JOIN (
    SELECT 'MONDAY' AS weekday, 0 AS sort_index
    UNION ALL SELECT 'WEDNESDAY', 1
) d ON 1=1
WHERE c.title = 'Seed: Java Grundlagen B (parallel)';
