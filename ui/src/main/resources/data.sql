INSERT INTO gender (created_at, name, description)
VALUES (CURRENT_TIMESTAMP, 'Male', 'Männlich');
INSERT INTO gender (created_at, name, description)
VALUES (CURRENT_TIMESTAMP, 'Female', 'Weiblich');

INSERT INTO address (created_at, street, house_number, zip_code, city, country)
VALUES (CURRENT_TIMESTAMP, 'Musterstrasse', '1', '8000', 'Zürich', 'Schweiz');

INSERT INTO course_level (created_at, code, name, description, sorting_order)
VALUES (CURRENT_TIMESTAMP, 'A1', 'Anfänger', 'Anfängerniveau', 1);

INSERT INTO course_schedule (created_at, week_start, week_end)
VALUES (CURRENT_TIMESTAMP, 1, 52);

INSERT INTO instructor (created_at, firstname, lastname, email, phone, gender, address)
VALUES (CURRENT_TIMESTAMP, 'Hans', 'Müller', 'hans.mueller@example.com', '+41791234567', 1, 1);

INSERT INTO parent (created_at, firstname, lastname, email, phone, gender, address)
VALUES (CURRENT_TIMESTAMP, 'Peter', 'Schmidt', 'peter.schmidt@example.com', '+41799876543', 1, 1);

INSERT INTO course (created_at, title, capacity, location, course_schedule_id, duration, instructor)
VALUES (CURRENT_TIMESTAMP, 'Grundkurs', 20, 'Schulzimmer 1', 1, 60, 1);

INSERT INTO course_weekday (course_id, weekday)
VALUES (1, 'MONDAY');

INSERT INTO participant (created_at, firstname, lastname, birthdate, gender, course_level, course, address, email,
                         phone, parent_one, parent_two)
VALUES (CURRENT_TIMESTAMP, 'Max', 'Muster', DATE '2010-05-12', 1, 1, 1, 1, 'max.muster@example.com', '+41791111111', 1,
        NULL);

