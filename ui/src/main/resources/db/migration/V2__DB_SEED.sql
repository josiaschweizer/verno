INSERT INTO public.mandants (id, slug, name)
VALUES (7777, 'default', 'Default Mandant')
    ON CONFLICT (id) DO NOTHING;

INSERT INTO public.mandants (id, slug, name)
VALUES (8888, 'demo', 'Demo Mandant')
    ON CONFLICT (id) DO NOTHING;

INSERT INTO public.gender (created_at, name, description)
SELECT CURRENT_TIMESTAMP, 'Male', 'Männlich'
    WHERE NOT EXISTS (SELECT 1 FROM public.gender WHERE name = 'Male');

INSERT INTO public.gender (created_at, name, description)
SELECT CURRENT_TIMESTAMP, 'Female', 'Weiblich'
    WHERE NOT EXISTS (SELECT 1 FROM public.gender WHERE name = 'Female');

INSERT INTO public.mandant_settings (
    mandant_id,
    course_weeks_per_schedule,
    max_participants_per_course,
    enforce_quantity_settings,
    enforce_course_level_settings,
    is_parent_one_main_parent,
    course_report_name,
    limit_course_assignments_to_active
)
SELECT
    7777,
    8,
    12,
    TRUE,
    FALSE,
    TRUE,
    'course-report-',
    FALSE
    WHERE NOT EXISTS (SELECT 1 FROM public.mandant_settings WHERE mandant_id = 7777);

INSERT INTO public.mandant_settings (
    mandant_id,
    course_weeks_per_schedule,
    max_participants_per_course,
    enforce_quantity_settings,
    enforce_course_level_settings,
    is_parent_one_main_parent,
    course_report_name,
    limit_course_assignments_to_active
)
SELECT
    8888,
    6,
    8,
    FALSE,
    FALSE,
    TRUE,
    'demo-course-report-',
    FALSE
    WHERE NOT EXISTS (SELECT 1 FROM public.mandant_settings WHERE mandant_id = 8888);

INSERT INTO public.address (created_at, mandant_id, street, house_number, zip_code, city, country)
SELECT NOW(), 7777, v.street, v.house_number, v.zip_code, v.city, v.country
FROM (VALUES
          ('Seestrasse', '12', '8002', 'Zürich', 'Schweiz'),
          ('Winterthurerstrasse', '88', '8057', 'Zürich', 'Schweiz'),
          ('Badenerstrasse', '210', '8004', 'Zürich', 'Schweiz'),
          ('Musterstrasse', '1', '8000', 'Zürich', 'Schweiz')
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

INSERT INTO public.address (created_at, mandant_id, street, house_number, zip_code, city, country)
SELECT NOW(), 8888, 'Demostrasse', '1', '9000', 'St. Gallen', 'Schweiz'
    WHERE NOT EXISTS (
    SELECT 1
    FROM public.address a
    WHERE a.mandant_id = 8888
      AND a.street = 'Demostrasse'
      AND a.house_number = '1'
      AND a.zip_code = '9000'
      AND a.city = 'St. Gallen'
      AND a.country = 'Schweiz'
);

INSERT INTO public.course_level (created_at, mandant_id, code, name, description, sorting_order)
SELECT NOW(), 7777, v.code, v.name, v.description, v.sorting_order
FROM (VALUES
          ('BAS-1', 'Basics 1', 'Wassergewöhnung, Atmung, Gleiten', 10),
          ('KRA-1', 'Kraul 1', 'Kraul-Beine, Atmung seitlich, Koordination', 20)
     ) AS v(code, name, description, sorting_order)
WHERE NOT EXISTS (
    SELECT 1 FROM public.course_level cl
    WHERE cl.mandant_id = 7777 AND cl.code = v.code
);

INSERT INTO public.course_level (created_at, mandant_id, code, name, description, sorting_order)
SELECT NOW(), 8888, 'DEMO-1', 'Demo Level', 'Minimaler Demo Course Level', 10
    WHERE NOT EXISTS (
    SELECT 1 FROM public.course_level cl
    WHERE cl.mandant_id = 8888 AND cl.code = 'DEMO-1'
);

INSERT INTO public.course_schedule (created_at, mandant_id, title, status, color)
SELECT NOW(), 7777, 'Winterblock A', 'PLANNED', '#55A6F6'
    WHERE NOT EXISTS (
    SELECT 1 FROM public.course_schedule cs
    WHERE cs.mandant_id = 7777 AND cs.title = 'Winterblock A'
);

INSERT INTO public.course_schedule (created_at, mandant_id, title, status, color)
SELECT NOW(), 8888, 'Demo Block', 'ACTIVE', '#55A6F6'
    WHERE NOT EXISTS (
    SELECT 1 FROM public.course_schedule cs
    WHERE cs.mandant_id = 8888 AND cs.title = 'Demo Block'
);

INSERT INTO public.course_schedule_week (mandant_id, course_schedule_id, sort_index, week)
SELECT 7777, cs.id, v.sort_index, v.week
FROM public.course_schedule cs
         JOIN (VALUES
                   (1, 'KW01-2026'),
                   (2, 'KW02-2026')
) AS v(sort_index, week) ON TRUE
WHERE cs.mandant_id = 7777 AND cs.title = 'Winterblock A'
  AND NOT EXISTS (
    SELECT 1
    FROM public.course_schedule_week csw
    WHERE csw.mandant_id = 7777
      AND csw.course_schedule_id = cs.id
      AND csw.week = v.week
);

INSERT INTO public.course_schedule_week (mandant_id, course_schedule_id, sort_index, week)
SELECT 8888, cs.id, 1, 'KW01-2026'
FROM public.course_schedule cs
WHERE cs.mandant_id = 8888 AND cs.title = 'Demo Block'
  AND NOT EXISTS (
    SELECT 1
    FROM public.course_schedule_week csw
    WHERE csw.mandant_id = 8888
      AND csw.course_schedule_id = cs.id
      AND csw.week = 'KW01-2026'
);

INSERT INTO public.instructor (created_at, mandant_id, firstname, lastname, email, phone, gender, address)
SELECT
    NOW(), 7777,
    'Lena', 'Meier',
    'lena.meier@swimschool.example',
    '+41 76 401 01 01',
    (SELECT id FROM public.gender WHERE name = 'Female' LIMIT 1),
    (SELECT id FROM public.address
     WHERE mandant_id = 7777 AND street = 'Winterthurerstrasse' AND house_number = '88' AND zip_code = '8057' AND city = 'Zürich' AND country = 'Schweiz'
     LIMIT 1)
WHERE NOT EXISTS (
    SELECT 1 FROM public.instructor i
    WHERE i.mandant_id = 7777 AND i.email = 'lena.meier@swimschool.example'
    );

INSERT INTO public.instructor (created_at, mandant_id, firstname, lastname, email, phone, gender, address)
SELECT
    NOW(), 8888,
    'Demo', 'Trainer',
    'demo.trainer@verno.local',
    '+41 79 000 00 00',
    (SELECT id FROM public.gender WHERE name = 'Male' LIMIT 1),
    (SELECT id FROM public.address
     WHERE mandant_id = 8888 AND street = 'Demostrasse' AND house_number = '1' AND zip_code = '9000' AND city = 'St. Gallen' AND country = 'Schweiz'
     LIMIT 1)
WHERE NOT EXISTS (
    SELECT 1 FROM public.instructor i
    WHERE i.mandant_id = 8888 AND i.email = 'demo.trainer@verno.local'
    );

INSERT INTO public.parent (created_at, mandant_id, firstname, lastname, email, phone, gender, address)
SELECT
    NOW(), 7777,
    'Sara', 'Meier',
    'sara.meier@example.com',
    '+41 79 100 00 01',
    (SELECT id FROM public.gender WHERE name = 'Female' LIMIT 1),
    (SELECT id FROM public.address
     WHERE mandant_id = 7777 AND street = 'Seestrasse' AND house_number = '12' AND zip_code = '8002' AND city = 'Zürich' AND country = 'Schweiz'
     LIMIT 1)
WHERE NOT EXISTS (
    SELECT 1 FROM public.parent p
    WHERE p.mandant_id = 7777 AND p.email = 'sara.meier@example.com'
    );

INSERT INTO public.parent (created_at, mandant_id, firstname, lastname, email, phone, gender, address)
SELECT
    NOW(), 8888,
    'Demo', 'Parent',
    'demo.parent@verno.local',
    '+41 79 000 00 02',
    (SELECT id FROM public.gender WHERE name = 'Female' LIMIT 1),
    (SELECT id FROM public.address
     WHERE mandant_id = 8888 AND street = 'Demostrasse' AND house_number = '1' AND zip_code = '9000' AND city = 'St. Gallen' AND country = 'Schweiz'
     LIMIT 1)
WHERE NOT EXISTS (
    SELECT 1 FROM public.parent p
    WHERE p.mandant_id = 8888 AND p.email = 'demo.parent@verno.local'
    );

WITH ins_participant AS (
INSERT INTO public.participant (
    created_at, mandant_id,
    firstname, lastname, birthdate,
    gender, address,
    email, phone,
    parent_one,
    active, note
)
SELECT
    NOW(), 7777,
    'Lena', 'Meier', DATE '2011-03-12',
    (SELECT id FROM public.gender WHERE name = 'Female' LIMIT 1),
        (SELECT id FROM public.address
         WHERE mandant_id = 7777 AND street = 'Musterstrasse' AND house_number = '1' AND zip_code = '8000' AND city = 'Zürich' AND country = 'Schweiz'
         LIMIT 1),
        'lena.meier@example.com',
        '+41 79 123 45 68',
        (SELECT id FROM public.parent
         WHERE mandant_id = 7777 AND email = 'sara.meier@example.com'
         LIMIT 1),
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
    (SELECT id FROM public.course_level WHERE mandant_id = 7777 AND code = 'BAS-1' LIMIT 1)
FROM ins_participant p
WHERE NOT EXISTS (
    SELECT 1
    FROM public.participant_course_level pcl
    WHERE pcl.mandant_id = 7777
  AND pcl.participant_id = p.id
  AND pcl.course_level_id = (SELECT id FROM public.course_level WHERE mandant_id = 7777 AND code = 'BAS-1' LIMIT 1)
    );

WITH ins_demo_participant AS (
INSERT INTO public.participant (
    created_at, mandant_id,
    firstname, lastname, birthdate,
    email, phone,
    parent_one,
    active, note
)
SELECT
    NOW(), 8888,
    'Demo', 'Teilnehmer', DATE '2012-01-01',
    'demo.teilnehmer@verno.local',
    '+41 79 000 00 01',
    (SELECT id FROM public.parent
     WHERE mandant_id = 8888 AND email = 'demo.parent@verno.local'
        LIMIT 1),
        TRUE,
        'seed: demo participant'
WHERE NOT EXISTS (
    SELECT 1 FROM public.participant p
    WHERE p.mandant_id = 8888 AND p.email = 'demo.teilnehmer@verno.local'
    )
    RETURNING id
    )
INSERT INTO public.participant_course_level (mandant_id, participant_id, course_level_id)
SELECT
    8888,
    p.id,
    (SELECT id FROM public.course_level WHERE mandant_id = 8888 AND code = 'DEMO-1' LIMIT 1)
FROM ins_demo_participant p
WHERE NOT EXISTS (
    SELECT 1
    FROM public.participant_course_level pcl
    WHERE pcl.mandant_id = 8888
  AND pcl.participant_id = p.id
  AND pcl.course_level_id = (SELECT id FROM public.course_level WHERE mandant_id = 8888 AND code = 'DEMO-1' LIMIT 1)
    );