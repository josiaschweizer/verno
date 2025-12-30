INSERT INTO address (created_at, street, house_number, zip_code, city, country)
SELECT NOW(), v.street, v.house_number, v.zip_code, v.city, v.country
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
             ('Hardturmstrasse', '161', '8005', 'Zürich',
              'Schweiz')) AS v(street, house_number, zip_code, city, country)
WHERE NOT EXISTS (SELECT 1
                  FROM address a
                  WHERE a.street = v.street
                    AND a.house_number = v.house_number
                    AND a.zip_code = v.zip_code
                    AND a.city = v.city
                    AND a.country = v.country);

INSERT INTO course_level (created_at, code, name, description, sorting_order)
SELECT NOW(), v.code, v.name, v.description, v.sorting_order
FROM (VALUES ('BAS-1', 'Basics 1', 'Wassergewöhnung, Atmung, Gleiten', 10),
             ('BAS-2', 'Basics 2', 'Schweben, Gleitlage, erste Arm-/Beinbewegungen', 20),
             ('KRA-1', 'Kraul 1', 'Kraul-Beine, Atmung seitlich, Koordination', 30),
             ('KRA-2', 'Kraul 2', 'Kraul-Technik, Ausdauer, Starts', 40),
             ('RUE-1', 'Rücken 1', 'Rückenlage, Rücken-Beine, Wasserlage', 50),
             ('RUE-2', 'Rücken 2', 'Rücken-Technik, Wenden, Rhythmus', 60),
             ('BRA-1', 'Brust 1', 'Brust-Beine, Gleitphase, Koordination', 70),
             ('BRA-2', 'Brust 2', 'Brust-Technik, Timing, Ausdauer', 80),
             ('DEL-1', 'Delfin 1', 'Delfin-Welle, Körperspannung, Grundlagen', 90),
             ('LIF-1', 'Life Saving', 'Sicherheit, Rettungsgriffe, Regeln',
              100)) AS v(code, name, description, sorting_order)
WHERE NOT EXISTS (SELECT 1 FROM course_level cl WHERE cl.code = v.code);

INSERT INTO course_schedule (created_at, title)
SELECT NOW(), v.title
FROM (VALUES ('Winterblock A'),
             ('Winterblock B'),
             ('Frühling A'),
             ('Frühling B'),
             ('Sommer A'),
             ('Sommer B'),
             ('Herbst A'),
             ('Herbst B'),
             ('Intensivwoche A'),
             ('Intensivwoche B')) AS v(title)
WHERE NOT EXISTS (SELECT 1 FROM course_schedule cs WHERE cs.title = v.title);

INSERT INTO course_schedule_week (course_schedule_id, sort_index, week)
SELECT cs.id, v.sort_index, v.week
FROM course_schedule cs
         JOIN (VALUES ('Winterblock A', 1, 'KW01-2026'),
                      ('Winterblock A', 2, 'KW02-2026'),
                      ('Winterblock A', 3, 'KW03-2026'),
                      ('Winterblock A', 4, 'KW04-2026'),
                      ('Winterblock B', 1, 'KW05-2026'),
                      ('Winterblock B', 2, 'KW06-2026'),
                      ('Winterblock B', 3, 'KW07-2026'),
                      ('Winterblock B', 4, 'KW08-2026'),
                      ('Frühling A', 1, 'KW10-2026'),
                      ('Frühling A', 2, 'KW11-2026'),
                      ('Frühling A', 3, 'KW12-2026'),
                      ('Frühling A', 4, 'KW13-2026'),
                      ('Frühling B', 1, 'KW14-2026'),
                      ('Frühling B', 2, 'KW15-2026'),
                      ('Frühling B', 3, 'KW16-2026'),
                      ('Frühling B', 4, 'KW17-2026'),
                      ('Sommer A', 1, 'KW20-2026'),
                      ('Sommer A', 2, 'KW21-2026'),
                      ('Sommer A', 3, 'KW22-2026'),
                      ('Sommer A', 4, 'KW23-2026'),
                      ('Sommer B', 1, 'KW24-2026'),
                      ('Sommer B', 2, 'KW25-2026'),
                      ('Sommer B', 3, 'KW26-2026'),
                      ('Sommer B', 4, 'KW27-2026'),
                      ('Herbst A', 1, 'KW36-2026'),
                      ('Herbst A', 2, 'KW37-2026'),
                      ('Herbst A', 3, 'KW38-2026'),
                      ('Herbst A', 4, 'KW39-2026'),
                      ('Herbst B', 1, 'KW40-2026'),
                      ('Herbst B', 2, 'KW41-2026'),
                      ('Herbst B', 3, 'KW42-2026'),
                      ('Herbst B', 4, 'KW43-2026'),
                      ('Intensivwoche A', 1, 'KW29-2026'),
                      ('Intensivwoche B', 1, 'KW30-2026')) AS v(schedule_title, sort_index, week)
              ON v.schedule_title = cs.title
WHERE NOT EXISTS (SELECT 1
                  FROM course_schedule_week csw
                  WHERE csw.course_schedule_id = cs.id
                    AND csw.week = v.week);

INSERT INTO instructor (created_at, firstname, lastname, email, phone, gender, address)
SELECT NOW(), v.firstname, v.lastname, v.email, v.phone, v.gender_id, v.address_id
FROM (VALUES ('Lena', 'Meier', 'lena.meier@swimschool.example', '+41 76 401 01 01',
              (SELECT id FROM gender WHERE name = 'Female'),
              (SELECT id
               FROM address
               WHERE street = 'Winterthurerstrasse'
                 AND house_number = '88'
                 AND zip_code = '8057'
                 AND city = 'Zürich'
                 AND country = 'Schweiz')),
             ('Noah', 'Keller', 'noah.keller@swimschool.example', '+41 76 401 01 02',
              (SELECT id FROM gender WHERE name = 'Male'),
              (SELECT id
               FROM address
               WHERE street = 'Badenerstrasse'
                 AND house_number = '210'
                 AND zip_code = '8004'
                 AND city = 'Zürich'
                 AND country = 'Schweiz')),
             ('Mia', 'Schmid', 'mia.schmid@swimschool.example', '+41 76 401 01 03',
              (SELECT id FROM gender WHERE name = 'Female'),
              (SELECT id
               FROM address
               WHERE street = 'Birmensdorferstrasse'
                 AND house_number = '15'
                 AND zip_code = '8003'
                 AND city = 'Zürich'
                 AND country = 'Schweiz')),
             ('Luca', 'Frei', 'luca.frei@swimschool.example', '+41 76 401 01 04',
              (SELECT id FROM gender WHERE name = 'Male'),
              (SELECT id
               FROM address
               WHERE street = 'Zürichbergstrasse'
                 AND house_number = '25'
                 AND zip_code = '8044'
                 AND city = 'Zürich'
                 AND country = 'Schweiz')),
             ('Emma', 'Weber', 'emma.weber@swimschool.example', '+41 76 401 01 05',
              (SELECT id FROM gender WHERE name = 'Female'),
              (SELECT id
               FROM address
               WHERE street = 'Albisriederstrasse'
                 AND house_number = '333'
                 AND zip_code = '8047'
                 AND city = 'Zürich'
                 AND country = 'Schweiz')),
             ('Ben', 'Graf', 'ben.graf@swimschool.example', '+41 76 401 01 06',
              (SELECT id FROM gender WHERE name = 'Male'),
              (SELECT id
               FROM address
               WHERE street = 'Seefeldstrasse'
                 AND house_number = '93'
                 AND zip_code = '8008'
                 AND city = 'Zürich'
                 AND country = 'Schweiz')),
             ('Sofia', 'Baumann', 'sofia.baumann@swimschool.example', '+41 76 401 01 07',
              (SELECT id FROM gender WHERE name = 'Female'),
              (SELECT id
               FROM address
               WHERE street = 'Hardturmstrasse'
                 AND house_number = '161'
                 AND zip_code = '8005'
                 AND city = 'Zürich'
                 AND country = 'Schweiz')),
             ('Finn', 'Huber', 'finn.huber@swimschool.example', '+41 76 401 01 08',
              (SELECT id FROM gender WHERE name = 'Male'),
              (SELECT id
               FROM address
               WHERE street = 'Bahnhofplatz'
                 AND house_number = '1'
                 AND zip_code = '8001'
                 AND city = 'Zürich'
                 AND country = 'Schweiz')),
             ('Lea', 'Kunz', 'lea.kunz@swimschool.example', '+41 76 401 01 09',
              (SELECT id FROM gender WHERE name = 'Female'),
              (SELECT id
               FROM address
               WHERE street = 'Hauptstrasse'
                 AND house_number = '77'
                 AND zip_code = '8800'
                 AND city = 'Thalwil'
                 AND country = 'Schweiz')),
             ('Jonas', 'Roth', 'jonas.roth@swimschool.example', '+41 76 401 01 10',
              (SELECT id FROM gender WHERE name = 'Male'),
              (SELECT id
               FROM address
               WHERE street = 'Zugerstrasse'
                 AND house_number = '19'
                 AND zip_code = '6300'
                 AND city = 'Zug'
                 AND country = 'Schweiz'))) AS v(firstname, lastname, email, phone, gender_id, address_id)
WHERE NOT EXISTS (SELECT 1 FROM instructor i WHERE i.email = v.email);

INSERT INTO course (created_at, title, capacity, location, course_schedule_id, start_time, end_time, instructor_id)
SELECT NOW(),
       v.title,
       v.capacity,
       v.location,
       v.course_schedule_id,
       v.start_time,
       v.end_time,
       v.instructor_id
FROM (VALUES ('Aqua Basics 1 - Montag 17:00', 10, 'Hallenbad City Zürich',
              (SELECT id FROM course_schedule WHERE title = 'Winterblock A'),
              TIME '17:00', TIME '17:45',
              (SELECT id FROM instructor WHERE email = 'lena.meier@swimschool.example')),
             ('Aqua Basics 2 - Montag 18:00', 10, 'Hallenbad City Zürich',
              (SELECT id FROM course_schedule WHERE title = 'Winterblock A'),
              TIME '18:00', TIME '18:45',
              (SELECT id FROM instructor WHERE email = 'noah.keller@swimschool.example')),
             ('Kraul 1 - Dienstag 17:00', 12, 'Hallenbad Oerlikon',
              (SELECT id FROM course_schedule WHERE title = 'Winterblock B'),
              TIME '17:00', TIME '17:50',
              (SELECT id FROM instructor WHERE email = 'mia.schmid@swimschool.example')),
             ('Kraul 2 - Dienstag 18:10', 12, 'Hallenbad Oerlikon',
              (SELECT id FROM course_schedule WHERE title = 'Winterblock B'),
              TIME '18:10', TIME '19:00',
              (SELECT id FROM instructor WHERE email = 'luca.frei@swimschool.example')),
             ('Rücken 1 - Mittwoch 16:30', 10, 'Hallenbad Seebach',
              (SELECT id FROM course_schedule WHERE title = 'Frühling A'),
              TIME '16:30', TIME '17:15',
              (SELECT id FROM instructor WHERE email = 'emma.weber@swimschool.example')),
             ('Rücken 2 - Mittwoch 17:30', 10, 'Hallenbad Seebach',
              (SELECT id FROM course_schedule WHERE title = 'Frühling A'),
              TIME '17:30', TIME '18:20',
              (SELECT id FROM instructor WHERE email = 'ben.graf@swimschool.example')),
             ('Brust 1 - Donnerstag 17:00', 12, 'Hallenbad Altstetten',
              (SELECT id FROM course_schedule WHERE title = 'Frühling B'),
              TIME '17:00', TIME '17:50',
              (SELECT id FROM instructor WHERE email = 'sofia.baumann@swimschool.example')),
             ('Brust 2 - Donnerstag 18:05', 12, 'Hallenbad Altstetten',
              (SELECT id FROM course_schedule WHERE title = 'Frühling B'),
              TIME '18:05', TIME '18:55',
              (SELECT id FROM instructor WHERE email = 'finn.huber@swimschool.example')),
             ('Delfin 1 - Freitag 17:00', 8, 'Hallenbad Letzigrund',
              (SELECT id FROM course_schedule WHERE title = 'Sommer A'),
              TIME '17:00', TIME '17:45',
              (SELECT id FROM instructor WHERE email = 'lea.kunz@swimschool.example')),
             ('Life Saving - Samstag 10:00', 10, 'Hallenbad City Zürich',
              (SELECT id FROM course_schedule WHERE title = 'Sommer B'),
              TIME '10:00', TIME '11:00',
              (SELECT id FROM instructor WHERE email = 'jonas.roth@swimschool.example'))) AS v(title, capacity,
                                                                                               location,
                                                                                               course_schedule_id,
                                                                                               start_time, end_time,
                                                                                               instructor_id)
WHERE NOT EXISTS (SELECT 1 FROM course c WHERE c.title = v.title);

INSERT INTO course_course_level (course_id, course_level_id, sort_index)
SELECT c.id, cl.id, v.sort_index
FROM (VALUES ('Aqua Basics 1 - Montag 17:00', 'BAS-1', 1),
             ('Aqua Basics 2 - Montag 18:00', 'BAS-2', 1),
             ('Kraul 1 - Dienstag 17:00', 'KRA-1', 1),
             ('Kraul 2 - Dienstag 18:10', 'KRA-2', 1),
             ('Rücken 1 - Mittwoch 16:30', 'RUE-1', 1),
             ('Rücken 2 - Mittwoch 17:30', 'RUE-2', 1),
             ('Brust 1 - Donnerstag 17:00', 'BRA-1', 1),
             ('Brust 2 - Donnerstag 18:05', 'BRA-2', 1),
             ('Delfin 1 - Freitag 17:00', 'DEL-1', 1),
             ('Life Saving - Samstag 10:00', 'LIF-1', 1)) AS v(course_title, level_code, sort_index)
         JOIN course c ON c.title = v.course_title
         JOIN course_level cl ON cl.code = v.level_code
WHERE NOT EXISTS (SELECT 1
                  FROM course_course_level ccl
                  WHERE ccl.course_id = c.id
                    AND ccl.course_level_id = cl.id
                    AND ccl.sort_index = v.sort_index);

INSERT INTO course_weekday (course_id, sort_index, weekday)
SELECT c.id, v.sort_index, v.weekday
FROM (VALUES ('Aqua Basics 1 - Montag 17:00', 1, 'MONDAY'),
             ('Aqua Basics 2 - Montag 18:00', 1, 'MONDAY'),
             ('Kraul 1 - Dienstag 17:00', 1, 'TUESDAY'),
             ('Kraul 2 - Dienstag 18:10', 1, 'TUESDAY'),
             ('Rücken 1 - Mittwoch 16:30', 1, 'WEDNESDAY'),
             ('Rücken 2 - Mittwoch 17:30', 1, 'WEDNESDAY'),
             ('Brust 1 - Donnerstag 17:00', 1, 'THURSDAY'),
             ('Brust 2 - Donnerstag 18:05', 1, 'THURSDAY'),
             ('Delfin 1 - Freitag 17:00', 1, 'FRIDAY'),
             ('Life Saving - Samstag 10:00', 1, 'SATURDAY')) AS v(course_title, sort_index, weekday)
         JOIN course c ON c.title = v.course_title
WHERE NOT EXISTS (SELECT 1
                  FROM course_weekday cw
                  WHERE cw.course_id = c.id
                    AND cw.weekday = v.weekday);

INSERT INTO parent (created_at, firstname, lastname, email, phone, gender, address)
SELECT NOW(), v.firstname, v.lastname, v.email, v.phone, v.gender_id, v.address_id
FROM (VALUES ('Markus', 'Müller', 'markus.mueller@family.example', '+41 76 301 01 01',
              (SELECT id FROM gender WHERE name = 'Male'),
              (SELECT id
               FROM address
               WHERE street = 'Seestrasse'
                 AND house_number = '12'
                 AND zip_code = '8002'
                 AND city = 'Zürich'
                 AND country = 'Schweiz')),
             ('Sabine', 'Müller', 'sabine.mueller@family.example', '+41 76 301 01 02',
              (SELECT id FROM gender WHERE name = 'Female'),
              (SELECT id
               FROM address
               WHERE street = 'Seestrasse'
                 AND house_number = '12'
                 AND zip_code = '8002'
                 AND city = 'Zürich'
                 AND country = 'Schweiz')),
             ('Daniel', 'Steiner', 'daniel.steiner@family.example', '+41 76 302 02 01',
              (SELECT id FROM gender WHERE name = 'Male'),
              (SELECT id
               FROM address
               WHERE street = 'Bahnhofstrasse'
                 AND house_number = '7'
                 AND zip_code = '8001'
                 AND city = 'Zürich'
                 AND country = 'Schweiz')),
             ('Nathalie', 'Steiner', 'nathalie.steiner@family.example', '+41 76 302 02 02',
              (SELECT id FROM gender WHERE name = 'Female'),
              (SELECT id
               FROM address
               WHERE street = 'Bahnhofstrasse'
                 AND house_number = '7'
                 AND zip_code = '8001'
                 AND city = 'Zürich'
                 AND country = 'Schweiz')),
             ('Patrick', 'Hug', 'patrick.hug@family.example', '+41 76 303 03 01',
              (SELECT id FROM gender WHERE name = 'Male'),
              (SELECT id
               FROM address
               WHERE street = 'Limmatquai'
                 AND house_number = '3'
                 AND zip_code = '8001'
                 AND city = 'Zürich'
                 AND country = 'Schweiz')),
             ('Julia', 'Hug', 'julia.hug@family.example', '+41 76 303 03 02',
              (SELECT id FROM gender WHERE name = 'Female'),
              (SELECT id
               FROM address
               WHERE street = 'Limmatquai'
                 AND house_number = '3'
                 AND zip_code = '8001'
                 AND city = 'Zürich'
                 AND country = 'Schweiz')),
             ('Michael', 'Bachmann', 'michael.bachmann@family.example', '+41 76 304 04 01',
              (SELECT id FROM gender WHERE name = 'Male'),
              (SELECT id
               FROM address
               WHERE street = 'Uetlibergstrasse'
                 AND house_number = '55'
                 AND zip_code = '8045'
                 AND city = 'Zürich'
                 AND country = 'Schweiz')),
             ('Carla', 'Bachmann', 'carla.bachmann@family.example', '+41 76 304 04 02',
              (SELECT id FROM gender WHERE name = 'Female'),
              (SELECT id
               FROM address
               WHERE street = 'Uetlibergstrasse'
                 AND house_number = '55'
                 AND zip_code = '8045'
                 AND city = 'Zürich'
                 AND country = 'Schweiz')),
             ('Thomas', 'Arnold', 'thomas.arnold@family.example', '+41 76 305 05 01',
              (SELECT id FROM gender WHERE name = 'Male'),
              (SELECT id
               FROM address
               WHERE street = 'Dübendorfstrasse'
                 AND house_number = '101'
                 AND zip_code = '8051'
                 AND city = 'Zürich'
                 AND country = 'Schweiz')),
             ('Elena', 'Arnold', 'elena.arnold@family.example', '+41 76 305 05 02',
              (SELECT id FROM gender WHERE name = 'Female'),
              (SELECT id
               FROM address
               WHERE street = 'Dübendorfstrasse'
                 AND house_number = '101'
                 AND zip_code = '8051'
                 AND city = 'Zürich'
                 AND country = 'Schweiz'))) AS v(firstname, lastname, email, phone, gender_id, address_id)
WHERE NOT EXISTS (SELECT 1 FROM parent p WHERE p.email = v.email);

INSERT INTO participant (created_at, firstname, lastname, birthdate, gender, course_level, course, address, email,
                         phone, parent_one, parent_two)
SELECT NOW(),
       v.firstname,
       v.lastname,
       v.birthdate,
       v.gender_id,
       v.course_level_id,
       v.course_id,
       v.address_id,
       v.email,
       v.phone,
       v.parent_one_id,
       v.parent_two_id
FROM (VALUES ('Tim', 'Müller', DATE '2014-03-12', (SELECT id FROM gender WHERE name = 'Male'),
              (SELECT id FROM course_level WHERE code = 'BAS-1'),
              (SELECT id FROM course WHERE title = 'Aqua Basics 1 - Montag 17:00'),
              (SELECT id
               FROM address
               WHERE street = 'Seestrasse'
                 AND house_number = '12'
                 AND zip_code = '8002'
                 AND city = 'Zürich'
                 AND country = 'Schweiz'),
              'tim.mueller@family.example', '+41 76 501 01 01',
              (SELECT id FROM parent WHERE email = 'markus.mueller@family.example'),
              (SELECT id FROM parent WHERE email = 'sabine.mueller@family.example')),
             ('Lina', 'Müller', DATE '2015-09-28', (SELECT id FROM gender WHERE name = 'Female'),
              (SELECT id FROM course_level WHERE code = 'BAS-1'),
              (SELECT id FROM course WHERE title = 'Aqua Basics 1 - Montag 17:00'),
              (SELECT id
               FROM address
               WHERE street = 'Seestrasse'
                 AND house_number = '12'
                 AND zip_code = '8002'
                 AND city = 'Zürich'
                 AND country = 'Schweiz'),
              'lina.mueller@family.example', '+41 76 501 01 02',
              (SELECT id FROM parent WHERE email = 'markus.mueller@family.example'),
              (SELECT id FROM parent WHERE email = 'sabine.mueller@family.example')),
             ('Noel', 'Steiner', DATE '2013-06-05', (SELECT id FROM gender WHERE name = 'Male'),
              (SELECT id FROM course_level WHERE code = 'BAS-2'),
              (SELECT id FROM course WHERE title = 'Aqua Basics 2 - Montag 18:00'),
              (SELECT id
               FROM address
               WHERE street = 'Bahnhofstrasse'
                 AND house_number = '7'
                 AND zip_code = '8001'
                 AND city = 'Zürich'
                 AND country = 'Schweiz'),
              'noel.steiner@family.example', '+41 76 502 02 01',
              (SELECT id FROM parent WHERE email = 'daniel.steiner@family.example'),
              (SELECT id FROM parent WHERE email = 'nathalie.steiner@family.example')),
             ('Mara', 'Steiner', DATE '2014-11-19', (SELECT id FROM gender WHERE name = 'Female'),
              (SELECT id FROM course_level WHERE code = 'BAS-2'),
              (SELECT id FROM course WHERE title = 'Aqua Basics 2 - Montag 18:00'),
              (SELECT id
               FROM address
               WHERE street = 'Bahnhofstrasse'
                 AND house_number = '7'
                 AND zip_code = '8001'
                 AND city = 'Zürich'
                 AND country = 'Schweiz'),
              'mara.steiner@family.example', '+41 76 502 02 02',
              (SELECT id FROM parent WHERE email = 'daniel.steiner@family.example'),
              (SELECT id FROM parent WHERE email = 'nathalie.steiner@family.example')),
             ('Elias', 'Hug', DATE '2012-02-21', (SELECT id FROM gender WHERE name = 'Male'),
              (SELECT id FROM course_level WHERE code = 'KRA-1'),
              (SELECT id FROM course WHERE title = 'Kraul 1 - Dienstag 17:00'),
              (SELECT id
               FROM address
               WHERE street = 'Limmatquai'
                 AND house_number = '3'
                 AND zip_code = '8001'
                 AND city = 'Zürich'
                 AND country = 'Schweiz'),
              'elias.hug@family.example', '+41 76 503 03 01',
              (SELECT id FROM parent WHERE email = 'patrick.hug@family.example'),
              (SELECT id FROM parent WHERE email = 'julia.hug@family.example')),
             ('Sara', 'Hug', DATE '2013-08-14', (SELECT id FROM gender WHERE name = 'Female'),
              (SELECT id FROM course_level WHERE code = 'KRA-1'),
              (SELECT id FROM course WHERE title = 'Kraul 1 - Dienstag 17:00'),
              (SELECT id
               FROM address
               WHERE street = 'Limmatquai'
                 AND house_number = '3'
                 AND zip_code = '8001'
                 AND city = 'Zürich'
                 AND country = 'Schweiz'),
              'sara.hug@family.example', '+41 76 503 03 02',
              (SELECT id FROM parent WHERE email = 'patrick.hug@family.example'),
              (SELECT id FROM parent WHERE email = 'julia.hug@family.example')),
             ('Leon', 'Bachmann', DATE '2011-05-10', (SELECT id FROM gender WHERE name = 'Male'),
              (SELECT id FROM course_level WHERE code = 'RUE-1'),
              (SELECT id FROM course WHERE title = 'Rücken 1 - Mittwoch 16:30'),
              (SELECT id
               FROM address
               WHERE street = 'Uetlibergstrasse'
                 AND house_number = '55'
                 AND zip_code = '8045'
                 AND city = 'Zürich'
                 AND country = 'Schweiz'),
              'leon.bachmann@family.example', '+41 76 504 04 01',
              (SELECT id FROM parent WHERE email = 'michael.bachmann@family.example'),
              (SELECT id FROM parent WHERE email = 'carla.bachmann@family.example')),
             ('Nina', 'Bachmann', DATE '2012-10-03', (SELECT id FROM gender WHERE name = 'Female'),
              (SELECT id FROM course_level WHERE code = 'RUE-1'),
              (SELECT id FROM course WHERE title = 'Rücken 1 - Mittwoch 16:30'),
              (SELECT id
               FROM address
               WHERE street = 'Uetlibergstrasse'
                 AND house_number = '55'
                 AND zip_code = '8045'
                 AND city = 'Zürich'
                 AND country = 'Schweiz'),
              'nina.bachmann@family.example', '+41 76 504 04 02',
              (SELECT id FROM parent WHERE email = 'michael.bachmann@family.example'),
              (SELECT id FROM parent WHERE email = 'carla.bachmann@family.example')),
             ('Paul', 'Arnold', DATE '2010-12-07', (SELECT id FROM gender WHERE name = 'Male'),
              (SELECT id FROM course_level WHERE code = 'BRA-1'),
              (SELECT id FROM course WHERE title = 'Brust 1 - Donnerstag 17:00'),
              (SELECT id
               FROM address
               WHERE street = 'Dübendorfstrasse'
                 AND house_number = '101'
                 AND zip_code = '8051'
                 AND city = 'Zürich'
                 AND country = 'Schweiz'),
              'paul.arnold@family.example', '+41 76 505 05 01',
              (SELECT id FROM parent WHERE email = 'thomas.arnold@family.example'),
              (SELECT id FROM parent WHERE email = 'elena.arnold@family.example')),
             ('Alina', 'Arnold', DATE '2011-07-30', (SELECT id FROM gender WHERE name = 'Female'),
              (SELECT id FROM course_level WHERE code = 'BRA-1'),
              (SELECT id FROM course WHERE title = 'Brust 1 - Donnerstag 17:00'),
              (SELECT id
               FROM address
               WHERE street = 'Dübendorfstrasse'
                 AND house_number = '101'
                 AND zip_code = '8051'
                 AND city = 'Zürich'
                 AND country = 'Schweiz'),
              'alina.arnold@family.example', '+41 76 505 05 02',
              (SELECT id FROM parent WHERE email = 'thomas.arnold@family.example'),
              (SELECT id FROM parent WHERE email = 'elena.arnold@family.example'))) AS v(firstname, lastname, birthdate,
                                                                                         gender_id, course_level_id,
                                                                                         course_id, address_id, email,
                                                                                         phone, parent_one_id,
                                                                                         parent_two_id)
WHERE NOT EXISTS (SELECT 1 FROM participant p WHERE p.email = v.email);

UPDATE participant p
SET course_level = (SELECT id FROM course_level WHERE code = 'BAS-1')
WHERE p.email IN ('tim.mueller@family.example', 'lina.mueller@family.example')
  AND p.course_level IS NULL;

UPDATE participant p
SET course_level = (SELECT id FROM course_level WHERE code = 'BAS-2')
WHERE p.email IN ('noel.steiner@family.example', 'mara.steiner@family.example')
  AND p.course_level IS NULL;

UPDATE participant p
SET course_level = (SELECT id FROM course_level WHERE code = 'KRA-1')
WHERE p.email IN ('elias.hug@family.example', 'sara.hug@family.example')
  AND p.course_level IS NULL;

UPDATE participant p
SET course_level = (SELECT id FROM course_level WHERE code = 'RUE-1')
WHERE p.email IN ('leon.bachmann@family.example', 'nina.bachmann@family.example')
  AND p.course_level IS NULL;

UPDATE participant p
SET course_level = (SELECT id FROM course_level WHERE code = 'BRA-1')
WHERE p.email IN ('paul.arnold@family.example', 'alina.arnold@family.example')
  AND p.course_level IS NULL;

UPDATE participant p
SET course = (SELECT id FROM course WHERE title = 'Aqua Basics 1 - Montag 17:00')
WHERE p.email IN ('tim.mueller@family.example', 'lina.mueller@family.example')
  AND p.course IS NULL;

UPDATE participant p
SET course = (SELECT id FROM course WHERE title = 'Aqua Basics 2 - Montag 18:00')
WHERE p.email IN ('noel.steiner@family.example', 'mara.steiner@family.example')
  AND p.course IS NULL;

UPDATE participant p
SET course = (SELECT id FROM course WHERE title = 'Kraul 1 - Dienstag 17:00')
WHERE p.email IN ('elias.hug@family.example', 'sara.hug@family.example')
  AND p.course IS NULL;

UPDATE participant p
SET course = (SELECT id FROM course WHERE title = 'Rücken 1 - Mittwoch 16:30')
WHERE p.email IN ('leon.bachmann@family.example', 'nina.bachmann@family.example')
  AND p.course IS NULL;

UPDATE participant p
SET course = (SELECT id FROM course WHERE title = 'Brust 1 - Donnerstag 17:00')
WHERE p.email IN ('paul.arnold@family.example', 'alina.arnold@family.example')
  AND p.course IS NULL;

UPDATE participant p
SET address = (SELECT id
               FROM address
               WHERE street = 'Seestrasse'
                 AND house_number = '12'
                 AND zip_code = '8002'
                 AND city = 'Zürich'
                 AND country = 'Schweiz')
WHERE p.email IN ('tim.mueller@family.example', 'lina.mueller@family.example')
  AND p.address IS NULL;

UPDATE participant p
SET address = (SELECT id
               FROM address
               WHERE street = 'Bahnhofstrasse'
                 AND house_number = '7'
                 AND zip_code = '8001'
                 AND city = 'Zürich'
                 AND country = 'Schweiz')
WHERE p.email IN ('noel.steiner@family.example', 'mara.steiner@family.example')
  AND p.address IS NULL;

UPDATE participant p
SET address = (SELECT id
               FROM address
               WHERE street = 'Limmatquai'
                 AND house_number = '3'
                 AND zip_code = '8001'
                 AND city = 'Zürich'
                 AND country = 'Schweiz')
WHERE p.email IN ('elias.hug@family.example', 'sara.hug@family.example')
  AND p.address IS NULL;

UPDATE participant p
SET address = (SELECT id
               FROM address
               WHERE street = 'Uetlibergstrasse'
                 AND house_number = '55'
                 AND zip_code = '8045'
                 AND city = 'Zürich'
                 AND country = 'Schweiz')
WHERE p.email IN ('leon.bachmann@family.example', 'nina.bachmann@family.example')
  AND p.address IS NULL;

UPDATE participant p
SET address = (SELECT id
               FROM address
               WHERE street = 'Dübendorfstrasse'
                 AND house_number = '101'
                 AND zip_code = '8051'
                 AND city = 'Zürich'
                 AND country = 'Schweiz')
WHERE p.email IN ('paul.arnold@family.example', 'alina.arnold@family.example')
  AND p.address IS NULL;

UPDATE instructor i
SET address = (SELECT id
               FROM address
               WHERE street = 'Winterthurerstrasse'
                 AND house_number = '88'
                 AND zip_code = '8057'
                 AND city = 'Zürich'
                 AND country = 'Schweiz')
WHERE i.email IN ('lena.meier@swimschool.example')
  AND i.address IS NULL;

UPDATE instructor i
SET address = (SELECT id
               FROM address
               WHERE street = 'Badenerstrasse'
                 AND house_number = '210'
                 AND zip_code = '8004'
                 AND city = 'Zürich'
                 AND country = 'Schweiz')
WHERE i.email IN ('noah.keller@swimschool.example')
  AND i.address IS NULL;

UPDATE instructor i
SET address = (SELECT id
               FROM address
               WHERE street = 'Birmensdorferstrasse'
                 AND house_number = '15'
                 AND zip_code = '8003'
                 AND city = 'Zürich'
                 AND country = 'Schweiz')
WHERE i.email IN ('mia.schmid@swimschool.example')
  AND i.address IS NULL;

UPDATE instructor i
SET address = (SELECT id
               FROM address
               WHERE street = 'Zürichbergstrasse'
                 AND house_number = '25'
                 AND zip_code = '8044'
                 AND city = 'Zürich'
                 AND country = 'Schweiz')
WHERE i.email IN ('luca.frei@swimschool.example')
  AND i.address IS NULL;

UPDATE instructor i
SET address = (SELECT id
               FROM address
               WHERE street = 'Albisriederstrasse'
                 AND house_number = '333'
                 AND zip_code = '8047'
                 AND city = 'Zürich'
                 AND country = 'Schweiz')
WHERE i.email IN ('emma.weber@swimschool.example')
  AND i.address IS NULL;

UPDATE participant p
SET parent_one = (SELECT id FROM parent WHERE email = 'markus.mueller@family.example'),
    parent_two = (SELECT id FROM parent WHERE email = 'sabine.mueller@family.example')
WHERE p.email IN ('tim.mueller@family.example', 'lina.mueller@family.example')
  AND (p.parent_one IS NULL OR p.parent_two IS NULL);

UPDATE participant p
SET parent_one = (SELECT id FROM parent WHERE email = 'daniel.steiner@family.example'),
    parent_two = (SELECT id FROM parent WHERE email = 'nathalie.steiner@family.example')
WHERE p.email IN ('noel.steiner@family.example', 'mara.steiner@family.example')
  AND (p.parent_one IS NULL OR p.parent_two IS NULL);

UPDATE participant p
SET parent_one = (SELECT id FROM parent WHERE email = 'patrick.hug@family.example'),
    parent_two = (SELECT id FROM parent WHERE email = 'julia.hug@family.example')
WHERE p.email IN ('elias.hug@family.example', 'sara.hug@family.example')
  AND (p.parent_one IS NULL OR p.parent_two IS NULL);

UPDATE participant p
SET parent_one = (SELECT id FROM parent WHERE email = 'michael.bachmann@family.example'),
    parent_two = (SELECT id FROM parent WHERE email = 'carla.bachmann@family.example')
WHERE p.email IN ('leon.bachmann@family.example', 'nina.bachmann@family.example')
  AND (p.parent_one IS NULL OR p.parent_two IS NULL);

UPDATE participant p
SET parent_one = (SELECT id FROM parent WHERE email = 'thomas.arnold@family.example'),
    parent_two = (SELECT id FROM parent WHERE email = 'elena.arnold@family.example')
WHERE p.email IN ('paul.arnold@family.example', 'alina.arnold@family.example')
  AND (p.parent_one IS NULL OR p.parent_two IS NULL);

INSERT INTO mandant_settings (course_weeks_per_schedule, max_participants_per_course, enforce_quantity_settings)
SELECT 4,
       10,
       TRUE WHERE NOT EXISTS (SELECT 1 FROM mandant_settings);