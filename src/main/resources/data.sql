INSERT INTO participant
(created_at, firstname, lastname, birthdate, gender, course_level, course, address, email, phone, parent_one,
 parent_two)
VALUES (CURRENT_TIMESTAMP, 'Max', 'Muster', DATE '2010-05-12', 1, 2, 3, 10, 'max.muster@example.com', '+41791234567',
        100, 101),
       (CURRENT_TIMESTAMP, 'Lea', 'Meier', DATE '2011-09-03', 2, 1, 4, 11, 'lea.meier@example.com', '+41799876543', 102,
        103);

INSERT INTO gender
    (created_at, name, description)
VALUES (CURRENT_TIMESTAMP, 'Male', 'Männlich'),
       (CURRENT_TIMESTAMP, 'Female', 'Weiblich');