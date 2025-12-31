INSERT INTO gender (created_at, name, description)
SELECT CURRENT_TIMESTAMP,
       'Male',
       'Männlich'
WHERE NOT EXISTS (SELECT 1 FROM gender WHERE name = 'Male');

INSERT INTO gender (created_at, name, description)
SELECT CURRENT_TIMESTAMP,
       'Female',
       'Weiblich'
WHERE NOT EXISTS (SELECT 1 FROM gender WHERE name = 'Female');

INSERT INTO gender (created_at, name, description)
SELECT CURRENT_TIMESTAMP,
       'asdf',
       'wadsf'
WHERE NOT EXISTS (SELECT 1 FROM gender WHERE name = 'asdf');

INSERT INTO address (created_at, street, house_number, zip_code, city, country)
SELECT CURRENT_TIMESTAMP,
       'Musterstrasse',
       '1',
       '8000',
       'Zürich',
       'Schweiz'
WHERE NOT EXISTS (SELECT 1
                  FROM address
                  WHERE street = 'Musterstrasse'
                    AND house_number = '1'
                    AND zip_code = '8000'
                    AND city = 'Zürich'
                    AND country = 'Schweiz');

INSERT INTO address (created_at, street, house_number, zip_code, city, country)
SELECT CURRENT_TIMESTAMP,
       'Bahnhofstrasse',
       '10',
       '9000',
       'St. Gallen',
       'Schweiz'
WHERE NOT EXISTS (SELECT 1
                  FROM address
                  WHERE street = 'Bahnhofstrasse'
                    AND house_number = '10'
                    AND zip_code = '9000'
                    AND city = 'St. Gallen'
                    AND country = 'Schweiz');

INSERT INTO address (created_at, street, house_number, zip_code, city, country)
SELECT CURRENT_TIMESTAMP,
       'Seestrasse',
       '5',
       '6003',
       'Luzern',
       'Schweiz'
WHERE NOT EXISTS (SELECT 1
                  FROM address
                  WHERE street = 'Seestrasse'
                    AND house_number = '5'
                    AND zip_code = '6003'
                    AND city = 'Luzern'
                    AND country = 'Schweiz');

INSERT INTO course_level (created_at, code, name, description, sorting_order)
SELECT CURRENT_TIMESTAMP,
       'A1',
       'Anfänger',
       'Anfängerniveau',
       1
WHERE NOT EXISTS (SELECT 1 FROM course_level WHERE code = 'A1');

INSERT INTO course_level (created_at, code, name, description, sorting_order)
SELECT CURRENT_TIMESTAMP,
       'A2',
       'Fortgeschritten',
       'Fortgeschrittenesniveau',
       2
WHERE NOT EXISTS (SELECT 1 FROM course_level WHERE code = 'A2');

INSERT INTO course_level (created_at, code, name, description, sorting_order)
SELECT CURRENT_TIMESTAMP,
       'B1',
       'Mittelstufe',
       'Solide Grundlagen',
       3
WHERE NOT EXISTS (SELECT 1 FROM course_level WHERE code = 'B1');

INSERT INTO course_schedule (created_at, title)
SELECT CURRENT_TIMESTAMP,
       'Ganzes Jahr'
WHERE NOT EXISTS (SELECT 1 FROM course_schedule WHERE title = 'Ganzes Jahr');

INSERT INTO course_schedule (created_at, title)
SELECT CURRENT_TIMESTAMP,
       'Frühling Block'
WHERE NOT EXISTS (SELECT 1 FROM course_schedule WHERE title = 'Frühling Block');

INSERT INTO course_schedule (created_at, title)
SELECT CURRENT_TIMESTAMP,
       'Sommer Block'
WHERE NOT EXISTS (SELECT 1 FROM course_schedule WHERE title = 'Sommer Block');

INSERT INTO course_schedule_week (course_schedule_id, sort_index, week)
SELECT (SELECT id FROM course_schedule WHERE title = 'Ganzes Jahr' ORDER BY id DESC LIMIT 1),
       v.sort_index,
       v.week
FROM (
         VALUES (0, 'KW01-2026'),
                (1, 'KW02-2026'),
                (2, 'KW03-2026'),
                (3, 'KW04-2026')
         ) AS v(sort_index, week)
WHERE NOT EXISTS (SELECT 1
                  FROM course_schedule_week csw
                  WHERE csw.course_schedule_id =
                        (SELECT id FROM course_schedule WHERE title = 'Ganzes Jahr' ORDER BY id DESC LIMIT 1)
                    AND csw.week = v.week);

INSERT INTO course_schedule_week (course_schedule_id, sort_index, week)
SELECT (SELECT id FROM course_schedule WHERE title = 'Frühling Block' ORDER BY id DESC LIMIT 1),
       v.sort_index,
       v.week
FROM (
         VALUES (0, 'KW10-2026'),
                (1, 'KW11-2026'),
                (2, 'KW12-2026'),
                (3, 'KW13-2026')
         ) AS v(sort_index, week)
WHERE NOT EXISTS (SELECT 1
                  FROM course_schedule_week csw
                  WHERE csw.course_schedule_id =
                        (SELECT id FROM course_schedule WHERE title = 'Frühling Block' ORDER BY id DESC LIMIT 1)
                    AND csw.week = v.week);

INSERT INTO course_schedule_week (course_schedule_id, sort_index, week)
SELECT (SELECT id FROM course_schedule WHERE title = 'Sommer Block' ORDER BY id DESC LIMIT 1),
       v.sort_index,
       v.week
FROM (
         VALUES (0, 'KW26-2026'),
                (1, 'KW27-2026'),
                (2, 'KW28-2026'),
                (3, 'KW29-2026')
         ) AS v(sort_index, week)
WHERE NOT EXISTS (SELECT 1
                  FROM course_schedule_week csw
                  WHERE csw.course_schedule_id =
                        (SELECT id FROM course_schedule WHERE title = 'Sommer Block' ORDER BY id DESC LIMIT 1)
                    AND csw.week = v.week);

INSERT INTO instructor (created_at, firstname, lastname, email, phone, gender, address)
SELECT CURRENT_TIMESTAMP,
       'Hans',
       'Müller',
       'hans.mueller@example.com',
       '+41791234567',
       (SELECT id FROM gender WHERE name = 'Male' ORDER BY id DESC LIMIT 1),
       (SELECT id
        FROM address
        WHERE street = 'Musterstrasse'
          AND house_number = '1'
          AND zip_code = '8000'
          AND city = 'Zürich'
          AND country = 'Schweiz'
        ORDER BY id DESC
        LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM instructor WHERE email = 'hans.mueller@example.com');

INSERT INTO instructor (created_at, firstname, lastname, email, phone, gender, address)
SELECT CURRENT_TIMESTAMP,
       'Laura',
       'Keller',
       'laura.keller@example.com',
       '+41791230001',
       (SELECT id FROM gender WHERE name = 'Female' ORDER BY id DESC LIMIT 1),
       (SELECT id
        FROM address
        WHERE street = 'Bahnhofstrasse'
          AND house_number = '10'
          AND zip_code = '9000'
          AND city = 'St. Gallen'
          AND country = 'Schweiz'
        ORDER BY id DESC
        LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM instructor WHERE email = 'laura.keller@example.com');

INSERT INTO instructor (created_at, firstname, lastname, email, phone, gender, address)
SELECT CURRENT_TIMESTAMP,
       'Marco',
       'Meier',
       'marco.meier@example.com',
       '+41791230002',
       (SELECT id FROM gender WHERE name = 'Male' ORDER BY id DESC LIMIT 1),
       (SELECT id
        FROM address
        WHERE street = 'Seestrasse'
          AND house_number = '5'
          AND zip_code = '6003'
          AND city = 'Luzern'
          AND country = 'Schweiz'
        ORDER BY id DESC
        LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM instructor WHERE email = 'marco.meier@example.com');

INSERT INTO parent (created_at, firstname, lastname, email, phone, gender, address)
SELECT CURRENT_TIMESTAMP,
       'Peter',
       'Schmidt',
       'peter.schmidt@example.com',
       '+41799876543',
       (SELECT id FROM gender WHERE name = 'Male' ORDER BY id DESC LIMIT 1),
       (SELECT id
        FROM address
        WHERE street = 'Musterstrasse'
          AND house_number = '1'
          AND zip_code = '8000'
          AND city = 'Zürich'
          AND country = 'Schweiz'
        ORDER BY id DESC
        LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM parent WHERE email = 'peter.schmidt@example.com');

INSERT INTO parent (created_at, firstname, lastname, email, phone, gender, address)
SELECT CURRENT_TIMESTAMP,
       'Andrina',
       'Schmidt',
       'andrina.schmidt@example.com',
       '+41774330626',
       (SELECT id FROM gender WHERE name = 'Female' ORDER BY id DESC LIMIT 1),
       (SELECT id
        FROM address
        WHERE street = 'Musterstrasse'
          AND house_number = '1'
          AND zip_code = '8000'
          AND city = 'Zürich'
          AND country = 'Schweiz'
        ORDER BY id DESC
        LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM parent WHERE email = 'andrina.schmidt@example.com');

INSERT INTO parent (created_at, firstname, lastname, email, phone, gender, address)
SELECT CURRENT_TIMESTAMP,
       'Nina',
       'Frei',
       'nina.frei@example.com',
       '+41791239999',
       (SELECT id FROM gender WHERE name = 'Female' ORDER BY id DESC LIMIT 1),
       (SELECT id
        FROM address
        WHERE street = 'Bahnhofstrasse'
          AND house_number = '10'
          AND zip_code = '9000'
          AND city = 'St. Gallen'
          AND country = 'Schweiz'
        ORDER BY id DESC
        LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM parent WHERE email = 'nina.frei@example.com');

INSERT INTO parent (created_at, firstname, lastname, email, phone, gender, address)
SELECT CURRENT_TIMESTAMP,
       'Thomas',
       'Frei',
       'thomas.frei@example.com',
       '+41791238888',
       (SELECT id FROM gender WHERE name = 'Male' ORDER BY id DESC LIMIT 1),
       (SELECT id
        FROM address
        WHERE street = 'Bahnhofstrasse'
          AND house_number = '10'
          AND zip_code = '9000'
          AND city = 'St. Gallen'
          AND country = 'Schweiz'
        ORDER BY id DESC
        LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM parent WHERE email = 'thomas.frei@example.com');

INSERT INTO course (created_at, title, capacity, location, course_schedule_id, start_time, end_time, instructor_id)
SELECT CURRENT_TIMESTAMP,
       'Grundkurs',
       20,
       'Schulzimmer 1',
       (SELECT id FROM course_schedule WHERE title = 'Ganzes Jahr' ORDER BY id DESC LIMIT 1),
       TIME '14:00',
       TIME '15:00',
       (SELECT id FROM instructor WHERE email = 'hans.mueller@example.com' ORDER BY id DESC LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM course WHERE title = 'Grundkurs');

INSERT INTO course (created_at, title, capacity, location, course_schedule_id, start_time, end_time, instructor_id)
SELECT CURRENT_TIMESTAMP,
       'Frühlingskurs',
       12,
       'Schulzimmer 2',
       (SELECT id FROM course_schedule WHERE title = 'Frühling Block' ORDER BY id DESC LIMIT 1),
       TIME '15:30',
       TIME '16:45',
       (SELECT id FROM instructor WHERE email = 'laura.keller@example.com' ORDER BY id DESC LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM course WHERE title = 'Frühlingskurs');

INSERT INTO course (created_at, title, capacity, location, course_schedule_id, start_time, end_time, instructor_id)
SELECT CURRENT_TIMESTAMP,
       'Sommerkurs',
       16,
       'Turnhalle',
       (SELECT id FROM course_schedule WHERE title = 'Sommer Block' ORDER BY id DESC LIMIT 1),
       TIME '17:00',
       TIME '18:30',
       (SELECT id FROM instructor WHERE email = 'marco.meier@example.com' ORDER BY id DESC LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM course WHERE title = 'Sommerkurs');

INSERT INTO course_course_level (course_id, course_level_id, sort_index)
SELECT c.id,
       cl.id,
       0
FROM course c,
     course_level cl
WHERE c.title = 'Grundkurs'
  AND cl.code = 'A1'
  AND NOT EXISTS (SELECT 1
                  FROM course_course_level x
                  WHERE x.course_id = c.id
                    AND x.course_level_id = cl.id
                    AND x.sort_index = 0);

INSERT INTO course_course_level (course_id, course_level_id, sort_index)
SELECT c.id,
       cl.id,
       0
FROM course c,
     course_level cl
WHERE c.title = 'Frühlingskurs'
  AND cl.code = 'A2'
  AND NOT EXISTS (SELECT 1
                  FROM course_course_level x
                  WHERE x.course_id = c.id
                    AND x.course_level_id = cl.id
                    AND x.sort_index = 0);

INSERT INTO course_course_level (course_id, course_level_id, sort_index)
SELECT c.id,
       cl.id,
       0
FROM course c,
     course_level cl
WHERE c.title = 'Sommerkurs'
  AND cl.code = 'B1'
  AND NOT EXISTS (SELECT 1
                  FROM course_course_level x
                  WHERE x.course_id = c.id
                    AND x.course_level_id = cl.id
                    AND x.sort_index = 0);

INSERT INTO course_weekday (course_id, sort_index, weekday)
SELECT (SELECT id FROM course WHERE title = 'Grundkurs' ORDER BY id DESC LIMIT 1), 0, 'MONDAY'
WHERE NOT EXISTS (SELECT 1
                  FROM course_weekday
                  WHERE course_id = (SELECT id FROM course WHERE title = 'Grundkurs' ORDER BY id DESC LIMIT 1)
                    AND weekday = 'MONDAY');

INSERT INTO course_weekday (course_id, sort_index, weekday)
SELECT (SELECT id FROM course WHERE title = 'Grundkurs' ORDER BY id DESC LIMIT 1), 1, 'WEDNESDAY'
WHERE NOT EXISTS (SELECT 1
                  FROM course_weekday
                  WHERE course_id = (SELECT id FROM course WHERE title = 'Grundkurs' ORDER BY id DESC LIMIT 1)
                    AND weekday = 'WEDNESDAY');

INSERT INTO course_weekday (course_id, sort_index, weekday)
SELECT (SELECT id FROM course WHERE title = 'Frühlingskurs' ORDER BY id DESC LIMIT 1), 0, 'TUESDAY'
WHERE NOT EXISTS (SELECT 1
                  FROM course_weekday
                  WHERE course_id = (SELECT id FROM course WHERE title = 'Frühlingskurs' ORDER BY id DESC LIMIT 1)
                    AND weekday = 'TUESDAY');

INSERT INTO course_weekday (course_id, sort_index, weekday)
SELECT (SELECT id FROM course WHERE title = 'Frühlingskurs' ORDER BY id DESC LIMIT 1), 1, 'THURSDAY'
WHERE NOT EXISTS (SELECT 1
                  FROM course_weekday
                  WHERE course_id = (SELECT id FROM course WHERE title = 'Frühlingskurs' ORDER BY id DESC LIMIT 1)
                    AND weekday = 'THURSDAY');

INSERT INTO course_weekday (course_id, sort_index, weekday)
SELECT (SELECT id FROM course WHERE title = 'Sommerkurs' ORDER BY id DESC LIMIT 1), 0, 'FRIDAY'
WHERE NOT EXISTS (SELECT 1
                  FROM course_weekday
                  WHERE course_id = (SELECT id FROM course WHERE title = 'Sommerkurs' ORDER BY id DESC LIMIT 1)
                    AND weekday = 'FRIDAY');

INSERT INTO participant (created_at, firstname, lastname, birthdate, gender, course_level, course, address, email,
                         phone, parent_one, parent_two)
SELECT CURRENT_TIMESTAMP,
       'Max',
       'Muster',
       DATE '2010-05-12',
       (SELECT id FROM gender WHERE name = 'Male' ORDER BY id DESC LIMIT 1),
       (SELECT id FROM course_level WHERE code = 'A1' ORDER BY id DESC LIMIT 1),
       (SELECT id FROM course WHERE title = 'Grundkurs' ORDER BY id DESC LIMIT 1),
       (SELECT id
        FROM address
        WHERE street = 'Musterstrasse'
          AND house_number = '1'
          AND zip_code = '8000'
          AND city = 'Zürich'
          AND country = 'Schweiz'
        ORDER BY id DESC
        LIMIT 1),
       'max.muster@example.com',
       '+41791111111',
       (SELECT id FROM parent WHERE email = 'peter.schmidt@example.com' ORDER BY id DESC LIMIT 1),
       (SELECT id FROM parent WHERE email = 'andrina.schmidt@example.com' ORDER BY id DESC LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM participant WHERE email = 'max.muster@example.com');

INSERT INTO participant (created_at, firstname, lastname, birthdate, gender, course_level, course, address, email,
                         phone, parent_one, parent_two)
SELECT CURRENT_TIMESTAMP,
       'Lea',
       'Frei',
       DATE '2011-02-03',
       (SELECT id FROM gender WHERE name = 'Female' ORDER BY id DESC LIMIT 1),
       (SELECT id FROM course_level WHERE code = 'A2' ORDER BY id DESC LIMIT 1),
       (SELECT id FROM course WHERE title = 'Frühlingskurs' ORDER BY id DESC LIMIT 1),
       (SELECT id
        FROM address
        WHERE street = 'Bahnhofstrasse'
          AND house_number = '10'
          AND zip_code = '9000'
          AND city = 'St. Gallen'
          AND country = 'Schweiz'
        ORDER BY id DESC
        LIMIT 1),
       'lea.frei@example.com',
       '+41792222222',
       (SELECT id FROM parent WHERE email = 'nina.frei@example.com' ORDER BY id DESC LIMIT 1),
       (SELECT id FROM parent WHERE email = 'thomas.frei@example.com' ORDER BY id DESC LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM participant WHERE email = 'lea.frei@example.com');

INSERT INTO participant (created_at, firstname, lastname, birthdate, gender, course_level, course, address, email,
                         phone, parent_one, parent_two)
SELECT CURRENT_TIMESTAMP,
       'Noah',
       'Kunz',
       DATE '2009-11-20',
       (SELECT id FROM gender WHERE name = 'Male' ORDER BY id DESC LIMIT 1),
       (SELECT id FROM course_level WHERE code = 'B1' ORDER BY id DESC LIMIT 1),
       (SELECT id FROM course WHERE title = 'Sommerkurs' ORDER BY id DESC LIMIT 1),
       (SELECT id
        FROM address
        WHERE street = 'Seestrasse'
          AND house_number = '5'
          AND zip_code = '6003'
          AND city = 'Luzern'
          AND country = 'Schweiz'
        ORDER BY id DESC
        LIMIT 1),
       'noah.kunz@example.com',
       '+41793333333',
       (SELECT id FROM parent WHERE email = 'peter.schmidt@example.com' ORDER BY id DESC LIMIT 1),
       (SELECT id FROM parent WHERE email = 'andrina.schmidt@example.com' ORDER BY id DESC LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM participant WHERE email = 'noah.kunz@example.com');

INSERT INTO mandant_settings (course_weeks_per_schedule, max_participants_per_course, enforce_quantity_settings)
SELECT 8, 12, true
WHERE NOT EXISTS (SELECT 1 FROM mandant_settings);



INSERT INTO participant (created_at, firstname, lastname, birthdate, gender, email)
SELECT CURRENT_TIMESTAMP,
       'Luca',
       'Weber',
       DATE '2010-08-14',
       (SELECT id FROM gender WHERE name = 'Male' ORDER BY id DESC LIMIT 1),
       'luca.weber@example.com'
WHERE NOT EXISTS (SELECT 1 FROM participant WHERE email = 'luca.weber@example.com');

INSERT INTO participant (created_at, firstname, lastname, birthdate, gender, email)
SELECT CURRENT_TIMESTAMP,
       'Mia',
       'Keller',
       DATE '2011-03-22',
       (SELECT id FROM gender WHERE name = 'Female' ORDER BY id DESC LIMIT 1),
       'mia.keller@example.com'
WHERE NOT EXISTS (SELECT 1 FROM participant WHERE email = 'mia.keller@example.com');

INSERT INTO participant (created_at, firstname, lastname, birthdate, gender, email)
SELECT CURRENT_TIMESTAMP,
       'Jonas',
       'Brunner',
       DATE '2009-12-02',
       (SELECT id FROM gender WHERE name = 'Male' ORDER BY id DESC LIMIT 1),
       'jonas.brunner@example.com'
WHERE NOT EXISTS (SELECT 1 FROM participant WHERE email = 'jonas.brunner@example.com');

INSERT INTO participant (created_at, firstname, lastname, birthdate, gender, email)
SELECT CURRENT_TIMESTAMP,
       'Lina',
       'Schneider',
       DATE '2010-01-19',
       (SELECT id FROM gender WHERE name = 'Female' ORDER BY id DESC LIMIT 1),
       'lina.schneider@example.com'
WHERE NOT EXISTS (SELECT 1 FROM participant WHERE email = 'lina.schneider@example.com');

INSERT INTO participant (created_at, firstname, lastname, birthdate, gender, email)
SELECT CURRENT_TIMESTAMP,
       'Finn',
       'Huber',
       DATE '2011-09-07',
       (SELECT id FROM gender WHERE name = 'Male' ORDER BY id DESC LIMIT 1),
       'finn.huber@example.com'
WHERE NOT EXISTS (SELECT 1 FROM participant WHERE email = 'finn.huber@example.com');

INSERT INTO participant (created_at, firstname, lastname, birthdate, gender, email)
SELECT CURRENT_TIMESTAMP,
       'Emma',
       'Baumann',
       DATE '2012-06-11',
       (SELECT id FROM gender WHERE name = 'Female' ORDER BY id DESC LIMIT 1),
       'emma.baumann@example.com'
WHERE NOT EXISTS (SELECT 1 FROM participant WHERE email = 'emma.baumann@example.com');

INSERT INTO participant (created_at, firstname, lastname, birthdate, gender, email)
SELECT CURRENT_TIMESTAMP,
       'Leon',
       'Fischer',
       DATE '2009-04-28',
       (SELECT id FROM gender WHERE name = 'Male' ORDER BY id DESC LIMIT 1),
       'leon.fischer@example.com'
WHERE NOT EXISTS (SELECT 1 FROM participant WHERE email = 'leon.fischer@example.com');

INSERT INTO participant (created_at, firstname, lastname, birthdate, gender, email)
SELECT CURRENT_TIMESTAMP,
       'Sofia',
       'Meier',
       DATE '2011-11-30',
       (SELECT id FROM gender WHERE name = 'Female' ORDER BY id DESC LIMIT 1),
       'sofia.meier@example.com'
WHERE NOT EXISTS (SELECT 1 FROM participant WHERE email = 'sofia.meier@example.com');

INSERT INTO participant (created_at, firstname, lastname, birthdate, gender, email)
SELECT CURRENT_TIMESTAMP,
       'Nico',
       'Roth',
       DATE '2010-10-05',
       (SELECT id FROM gender WHERE name = 'Male' ORDER BY id DESC LIMIT 1),
       'nico.roth@example.com'
WHERE NOT EXISTS (SELECT 1 FROM participant WHERE email = 'nico.roth@example.com');

INSERT INTO participant (created_at, firstname, lastname, birthdate, gender, email)
SELECT CURRENT_TIMESTAMP,
       'Elena',
       'Graf',
       DATE '2012-02-17',
       (SELECT id FROM gender WHERE name = 'Female' ORDER BY id DESC LIMIT 1),
       'elena.graf@example.com'
WHERE NOT EXISTS (SELECT 1 FROM participant WHERE email = 'elena.graf@example.com');

INSERT INTO participant (created_at, firstname, lastname, birthdate, gender, email)
SELECT CURRENT_TIMESTAMP,
       'Tim',
       'Bachmann',
       DATE '2009-07-13',
       (SELECT id FROM gender WHERE name = 'Male' ORDER BY id DESC LIMIT 1),
       'tim.bachmann@example.com'
WHERE NOT EXISTS (SELECT 1 FROM participant WHERE email = 'tim.bachmann@example.com');

INSERT INTO participant (created_at, firstname, lastname, birthdate, gender, email)
SELECT CURRENT_TIMESTAMP,
       'Alina',
       'Hug',
       DATE '2010-09-21',
       (SELECT id FROM gender WHERE name = 'Female' ORDER BY id DESC LIMIT 1),
       'alina.hug@example.com'
WHERE NOT EXISTS (SELECT 1 FROM participant WHERE email = 'alina.hug@example.com');

INSERT INTO participant (created_at, firstname, lastname, birthdate, gender, email)
SELECT CURRENT_TIMESTAMP,
       'Samuel',
       'Zimmermann',
       DATE '2011-05-03',
       (SELECT id FROM gender WHERE name = 'Male' ORDER BY id DESC LIMIT 1),
       'samuel.zimmermann@example.com'
WHERE NOT EXISTS (SELECT 1 FROM participant WHERE email = 'samuel.zimmermann@example.com');

INSERT INTO participant (created_at, firstname, lastname, birthdate, gender, email)
SELECT CURRENT_TIMESTAMP,
       'Chiara',
       'Egli',
       DATE '2012-08-26',
       (SELECT id FROM gender WHERE name = 'Female' ORDER BY id DESC LIMIT 1),
       'chiara.egli@example.com'
WHERE NOT EXISTS (SELECT 1 FROM participant WHERE email = 'chiara.egli@example.com');

INSERT INTO participant (created_at, firstname, lastname, birthdate, gender, email)
SELECT CURRENT_TIMESTAMP,
       'David',
       'Suter',
       DATE '2009-01-08',
       (SELECT id FROM gender WHERE name = 'Male' ORDER BY id DESC LIMIT 1),
       'david.suter@example.com'
WHERE NOT EXISTS (SELECT 1 FROM participant WHERE email = 'david.suter@example.com');