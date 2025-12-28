INSERT INTO gender (created_at, name, description)
VALUES (CURRENT_TIMESTAMP, 'Male', 'Männlich');
INSERT INTO gender (created_at, name, description)
VALUES (CURRENT_TIMESTAMP, 'Female', 'Weiblich');

INSERT INTO address (created_at, street, house_number, zip_code, city, country)
VALUES (CURRENT_TIMESTAMP, 'Musterstrasse', '1', '8000', 'Zürich', 'Schweiz'),
       (CURRENT_TIMESTAMP, 'Bahnhofstrasse', '10', '9000', 'St. Gallen', 'Schweiz'),
       (CURRENT_TIMESTAMP, 'Seestrasse', '5', '6003', 'Luzern', 'Schweiz');

INSERT INTO course_level (created_at, code, name, description, sorting_order)
VALUES (CURRENT_TIMESTAMP, 'A1', 'Anfänger', 'Anfängerniveau', 1),
       (CURRENT_TIMESTAMP, 'A2', 'Fortgeschritten', 'Fortgeschrittenesniveau', 2),
       (CURRENT_TIMESTAMP, 'B1', 'Mittelstufe', 'Solide Grundlagen', 3);

INSERT INTO course_schedule (created_at, title, weeks)
VALUES (CURRENT_TIMESTAMP, 'Ganzes Jahr', '["KW01-2026","KW02-2026","KW03-2026","KW04-2026"]'),
       (CURRENT_TIMESTAMP, 'Frühling Block', '["KW10-2026","KW11-2026","KW12-2026","KW13-2026"]'),
       (CURRENT_TIMESTAMP, 'Sommer Block', '["KW26-2026","KW27-2026","KW28-2026","KW29-2026"]');

INSERT INTO instructor (created_at, firstname, lastname, email, phone, gender, address)
VALUES (CURRENT_TIMESTAMP, 'Hans', 'Müller', 'hans.mueller@example.com', '+41791234567', 1, 1),
       (CURRENT_TIMESTAMP, 'Laura', 'Keller', 'laura.keller@example.com', '+41791230001', 2, 2),
       (CURRENT_TIMESTAMP, 'Marco', 'Meier', 'marco.meier@example.com', '+41791230002', 1, 3);

INSERT INTO parent (created_at, firstname, lastname, email, phone, gender, address)
VALUES (CURRENT_TIMESTAMP, 'Peter', 'Schmidt', 'peter.schmidt@example.com', '+41799876543', 1, 1),
       (CURRENT_TIMESTAMP, 'Andrina', 'Schmidt', 'andrina.schmidt@example.com', '+41774330626', 2, 1),
       (CURRENT_TIMESTAMP, 'Nina', 'Frei', 'nina.frei@example.com', '+41791239999', 2, 2),
       (CURRENT_TIMESTAMP, 'Thomas', 'Frei', 'thomas.frei@example.com', '+41791238888', 1, 2);

INSERT INTO course (created_at, title, capacity, location, course_schedule_id, duration, instructor_id)
VALUES (CURRENT_TIMESTAMP, 'Grundkurs', 20, 'Schulzimmer 1', 1, 60, 1),
       (CURRENT_TIMESTAMP, 'Frühlingskurs', 12, 'Schulzimmer 2', 2, 75, 2),
       (CURRENT_TIMESTAMP, 'Sommerkurs', 16, 'Turnhalle', 3, 90, 3);

INSERT INTO course_course_level (course_id, course_level_id, sort_index)
VALUES (1, 1, 0),
       (2, 2, 0),
       (3, 3, 0);

INSERT INTO course_weekday (course_id, sort_index, weekday)
VALUES (1, 0, 'MONDAY'),
       (1, 1, 'WEDNESDAY'),
       (2, 0, 'TUESDAY'),
       (2, 1, 'THURSDAY'),
       (3, 0, 'FRIDAY');

INSERT INTO participant (created_at, firstname, lastname, birthdate, gender, course_level, course, address, email,
                         phone, parent_one, parent_two)
VALUES (CURRENT_TIMESTAMP, 'Max', 'Muster', DATE '2010-05-12', 1, 1, 1, 1, 'max.muster@example.com', '+41791111111', 1, 2),
       (CURRENT_TIMESTAMP, 'Lea', 'Frei', DATE '2011-02-03', 2, 2, 2, 2, 'lea.frei@example.com', '+41792222222', 3, 4),
       (CURRENT_TIMESTAMP, 'Noah', 'Kunz', DATE '2009-11-20', 1, 3, 3, 3, 'noah.kunz@example.com', '+41793333333', 1, 2);