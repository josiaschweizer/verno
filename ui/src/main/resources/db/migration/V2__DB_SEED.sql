-- V2__DB_SEED.sql
-- Clean seed: matches V1 schema (no legacy participant.course/course_level).

-- Ensure default mandant exists
INSERT INTO public.mandants (id, slug, name)
VALUES (7777, 'default', 'Default Mandant')
    ON CONFLICT (id) DO NOTHING;

-- =========
-- Seed reference data
-- =========
INSERT INTO public.gender (created_at, name, description)
SELECT CURRENT_TIMESTAMP, 'Male', 'Männlich'
    WHERE NOT EXISTS (SELECT 1 FROM public.gender WHERE name = 'Male');

INSERT INTO public.gender (created_at, name, description)
SELECT CURRENT_TIMESTAMP, 'Female', 'Weiblich'
    WHERE NOT EXISTS (SELECT 1 FROM public.gender WHERE name = 'Female');

-- Settings: one row
INSERT INTO public.mandant_settings (
    course_weeks_per_schedule,
    max_participants_per_course,
    enforce_quantity_settings,
    enforce_course_level_settings,
    is_parent_one_main_parent,
    course_report_name,
    limit_course_assignments_to_active
)
SELECT
    8,
    12,
    TRUE,
    FALSE,
    TRUE,
    'course-report-',
    FALSE
    WHERE NOT EXISTS (SELECT 1 FROM public.mandant_settings);

-- =========
-- Addresses (example block from your seed, now with mandant_id)
-- =========
INSERT INTO public.address (created_at, mandant_id, street, house_number, zip_code, city, country)
SELECT NOW(), 7777, v.street, v.house_number, v.zip_code, v.city, v.country
FROM (VALUES ('Seestrasse', '12', '8002', 'Zürich', 'Schweiz'),
             ('Bahnhofstrasse', '7', '8001', 'Zürich', 'Schweiz'),
             ('Limmatquai', '3', '8001', 'Zürich', 'Schweiz'),
             ('Uetlibergstrasse', '55', '8045', 'Zürich', 'Schweiz'),
             ('Dübendorfstrasse', '101', '8051', 'Zürich', 'Schweiz'),
             ('Winterthurerstrasse', '88', '8057', 'Zürich', 'Schweiz'),
             ('Badenerstrasse', '210', '8004', 'Zürich', 'Schweiz'),
             ('Birmensdorferstrasse', '15', '8003', 'Zürich', 'Schweiz'),
             ('Zürichbergstrasse', '25', '8044', 'Zürich', 'Schweiz'),
             ('Albisriederstrasse', '333', '8047', 'Zürich', 'Schweiz'),
             ('Zugerstrasse', '19', '6300', 'Zug', 'Schweiz'),
             ('St. Gallerstrasse', '41', '9000', 'St. Gallen', 'Schweiz'),
             ('Pilatusstrasse', '6', '6003', 'Luzern', 'Schweiz'),
             ('Aeschenplatz', '2', '4052', 'Basel', 'Schweiz'),
             ('Rue du Rhône', '54', '1204', 'Genève', 'Schweiz'),
             ('Waisenhausplatz', '10', '3011', 'Bern', 'Schweiz'),
             ('Bahnhofplatz', '1', '8001', 'Zürich', 'Schweiz'),
             ('Hauptstrasse', '77', '8800', 'Thalwil', 'Schweiz'),
             ('Seefeldstrasse', '93', '8008', 'Zürich', 'Schweiz'),
             ('Hardturmstrasse', '161', '8005', 'Zürich', 'Schweiz')
     ) AS v(street, house_number, zip_code, city, country)
WHERE NOT EXISTS (
    SELECT 1
    FROM public.address a
    WHERE a.mandant_id = 7777
      AND a.street = v.street
      AND a.house_number = v.house_number
      AND a.zip_code = v.zip_code
      AND a.city = v.city
      AND a.country = v.country
);

-- =========
-- Course levels (tenant-scoped unique now)
-- =========
INSERT INTO public.course_level (created_at, mandant_id, code, name, description, sorting_order)
SELECT NOW(), 7777, v.code, v.name, v.description, v.sorting_order
FROM (VALUES ('BAS-1', 'Basics 1', 'Wassergewöhnung, Atmung, Gleiten', 10),
             ('BAS-2', 'Basics 2', 'Schweben, Gleitlage, erste Arm-/Beinbewegungen', 20),
             ('KRA-1', 'Kraul 1', 'Kraul-Beine, Atmung seitlich, Koordination', 30),
             ('KRA-2', 'Kraul 2', 'Kraul-Technik, Ausdauer, Starts', 40),
             ('RUE-1', 'Rücken 1', 'Rückenlage, Rücken-Beine, Wasserlage', 50),
             ('RUE-2', 'Rücken 2', 'Rücken-Technik, Wenden, Rhythmus', 60),
             ('BRA-1', 'Brust 1', 'Brust-Beine, Gleitphase, Koordination', 70),
             ('BRA-2', 'Brust 2', 'Brust-Technik, Timing, Ausdauer', 80),
             ('DEL-1', 'Delfin 1', 'Delfin-Welle, Körperspannung, Grundlagen', 90),
             ('LIF-1', 'Life Saving', 'Sicherheit, Rettungsgriffe, Regeln', 100)
     ) AS v(code, name, description, sorting_order)
WHERE NOT EXISTS (
    SELECT 1 FROM public.course_level cl
    WHERE cl.mandant_id = 7777 AND cl.code = v.code
);

-- =========
-- Course schedules
-- =========
INSERT INTO public.course_schedule (created_at, mandant_id, title)
SELECT NOW(), 7777, v.title
FROM (VALUES ('Winterblock A'),
             ('Winterblock B'),
             ('Frühling A'),
             ('Frühling B'),
             ('Sommer A'),
             ('Sommer B'),
             ('Herbst A'),
             ('Herbst B'),
             ('Intensivwoche A'),
             ('Intensivwoche B')
     ) AS v(title)
WHERE NOT EXISTS (
    SELECT 1 FROM public.course_schedule cs
    WHERE cs.mandant_id = 7777 AND cs.title = v.title
);

-- Course schedule weeks (tenant-scoped)
INSERT INTO public.course_schedule_week (mandant_id, course_schedule_id, sort_index, week)
SELECT
    7777,
    cs.id,
    v.sort_index,
    v.week
FROM public.course_schedule cs
         JOIN (VALUES ('Winterblock A', 1, 'KW01-2026'),
                      ('Winterblock A', 2, 'KW02-2026'),
                      ('Winterblock A', 3, 'KW03-2026'),
                      ('Winterblock A', 4, 'KW04-2026'),
                      ('Winterblock B', 1, 'KW05-2026'),
                      ('Winterblock B', 2, 'KW06-2026'),
                      ('Winterblock B', 3, 'KW07-2026'),
                      ('Winterblock B', 4, 'KW08-2026')
) AS v(schedule_title, sort_index, week)
              ON v.schedule_title = cs.title
WHERE cs.mandant_id = 7777
  AND NOT EXISTS (
    SELECT 1
    FROM public.course_schedule_week csw
    WHERE csw.mandant_id = 7777
      AND csw.course_schedule_id = cs.id
      AND csw.week = v.week
);

-- =========
-- Instructors (example – erweitere nach deinem Pattern)
-- =========
INSERT INTO public.instructor (created_at, mandant_id, firstname, lastname, email, phone, gender, address)
SELECT NOW(), 7777, v.firstname, v.lastname, v.email, v.phone, v.gender_id, v.address_id
FROM (
         VALUES
             ('Lena', 'Meier', 'lena.meier@swimschool.example', '+41 76 401 01 01',
              (SELECT id FROM public.gender WHERE name = 'Female'),
              (SELECT id FROM public.address
               WHERE mandant_id=7777 AND street='Winterthurerstrasse' AND house_number='88' AND zip_code='8057' AND city='Zürich' AND country='Schweiz'
                  LIMIT 1)),
     ('Noah', 'Keller', 'noah.keller@swimschool.example', '+41 76 401 01 02',
     (SELECT id FROM public.gender WHERE name = 'Male'),
     (SELECT id FROM public.address
      WHERE mandant_id=7777 AND street='Badenerstrasse' AND house_number='210' AND zip_code='8004' AND city='Zürich' AND country='Schweiz'
      LIMIT 1))
    ) AS v(firstname, lastname, email, phone, gender_id, address_id)
WHERE NOT EXISTS (
    SELECT 1 FROM public.instructor i
    WHERE i.mandant_id = 7777 AND i.email = v.email
    );

-- =========
-- Seed: inactive participant (fixed for new join-table model)
-- =========
WITH ins_participant AS (
INSERT INTO public.participant (
    created_at, mandant_id,
    firstname, lastname, birthdate,
    gender, address,
    email, phone,
    active, note
)
SELECT
            CURRENT_TIMESTAMP, 7777,
            'Lena', 'Meier', DATE '2011-03-12',
            (SELECT id FROM public.gender WHERE name = 'Female' LIMIT 1),
    (SELECT id
     FROM public.address
     WHERE mandant_id=7777
       AND street = 'Musterstrasse'
       AND house_number = '1'
       AND zip_code = '8000'
       AND city = 'Zürich'
       AND country = 'Schweiz'
     LIMIT 1),
    'lena.meier@example.com',
    '+41791234568',
    FALSE,
    'seed: inactive participant'
WHERE NOT EXISTS (
    SELECT 1 FROM public.participant p
    WHERE p.mandant_id = 7777 AND p.email = 'lena.meier@example.com'
    )
    RETURNING id
    )
INSERT INTO public.participant_course_level (mandant_id, participant_id, course_level_id)
SELECT
    7777,
    p.id,
    (SELECT id FROM public.course_level WHERE mandant_id=7777 AND code = 'BAS-1' LIMIT 1)
FROM ins_participant p
WHERE NOT EXISTS (
    SELECT 1
    FROM public.participant_course_level pcl
    WHERE pcl.mandant_id = 7777
  AND pcl.participant_id = p.id
  AND pcl.course_level_id = (SELECT id FROM public.course_level WHERE mandant_id=7777 AND code = 'BAS-1' LIMIT 1)
    );

-- =========
-- Optional: further prod seed (courses, parents, participants, relations)
-- -> Wichtig: überall mandant_id=7777 mitspeichern
-- -> Participant->Course / Participant->CourseLevel nur über join tables
-- =========