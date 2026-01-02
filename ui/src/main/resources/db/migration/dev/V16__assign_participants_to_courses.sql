INSERT INTO participant_course (participant_id, course_id)
SELECT p.id, c.id
FROM participant p, course c
WHERE p.email = 'luca.weber@example.com'
  AND c.title = 'Grundkurs'
  AND NOT EXISTS (
    SELECT 1 FROM participant_course pc
    WHERE pc.participant_id = p.id AND pc.course_id = c.id
);

INSERT INTO participant_course (participant_id, course_id)
SELECT p.id, c.id
FROM participant p, course c
WHERE p.email = 'jonas.brunner@example.com'
  AND c.title = 'Grundkurs'
  AND NOT EXISTS (
    SELECT 1 FROM participant_course pc
    WHERE pc.participant_id = p.id AND pc.course_id = c.id
);

INSERT INTO participant_course (participant_id, course_id)
SELECT p.id, c.id
FROM participant p, course c
WHERE p.email = 'finn.huber@example.com'
  AND c.title = 'Grundkurs'
  AND NOT EXISTS (
    SELECT 1 FROM participant_course pc
    WHERE pc.participant_id = p.id AND pc.course_id = c.id
);

INSERT INTO participant_course (participant_id, course_id)
SELECT p.id, c.id
FROM participant p, course c
WHERE p.email = 'nico.roth@example.com'
  AND c.title = 'Grundkurs'
  AND NOT EXISTS (
    SELECT 1 FROM participant_course pc
    WHERE pc.participant_id = p.id AND pc.course_id = c.id
);

INSERT INTO participant_course (participant_id, course_id)
SELECT p.id, c.id
FROM participant p, course c
WHERE p.email = 'mia.keller@example.com'
  AND c.title = 'Frühlingskurs'
  AND NOT EXISTS (
    SELECT 1 FROM participant_course pc
    WHERE pc.participant_id = p.id AND pc.course_id = c.id
);

INSERT INTO participant_course (participant_id, course_id)
SELECT p.id, c.id
FROM participant p, course c
WHERE p.email = 'lina.schneider@example.com'
  AND c.title = 'Frühlingskurs'
  AND NOT EXISTS (
    SELECT 1 FROM participant_course pc
    WHERE pc.participant_id = p.id AND pc.course_id = c.id
);

INSERT INTO participant_course (participant_id, course_id)
SELECT p.id, c.id
FROM participant p, course c
WHERE p.email = 'emma.baumann@example.com'
  AND c.title = 'Frühlingskurs'
  AND NOT EXISTS (
    SELECT 1 FROM participant_course pc
    WHERE pc.participant_id = p.id AND pc.course_id = c.id
);

INSERT INTO participant_course (participant_id, course_id)
SELECT p.id, c.id
FROM participant p, course c
WHERE p.email = 'sofia.meier@example.com'
  AND c.title = 'Frühlingskurs'
  AND NOT EXISTS (
    SELECT 1 FROM participant_course pc
    WHERE pc.participant_id = p.id AND pc.course_id = c.id
);

INSERT INTO participant_course (participant_id, course_id)
SELECT p.id, c.id
FROM participant p, course c
WHERE p.email = 'elena.graf@example.com'
  AND c.title = 'Frühlingskurs'
  AND NOT EXISTS (
    SELECT 1 FROM participant_course pc
    WHERE pc.participant_id = p.id AND pc.course_id = c.id
);

INSERT INTO participant_course (participant_id, course_id)
SELECT p.id, c.id
FROM participant p, course c
WHERE p.email = 'leon.fischer@example.com'
  AND c.title = 'Sommerkurs'
  AND NOT EXISTS (
    SELECT 1 FROM participant_course pc
    WHERE pc.participant_id = p.id AND pc.course_id = c.id
);

INSERT INTO participant_course (participant_id, course_id)
SELECT p.id, c.id
FROM participant p, course c
WHERE p.email = 'tim.bachmann@example.com'
  AND c.title = 'Sommerkurs'
  AND NOT EXISTS (
    SELECT 1 FROM participant_course pc
    WHERE pc.participant_id = p.id AND pc.course_id = c.id
);

INSERT INTO participant_course (participant_id, course_id)
SELECT p.id, c.id
FROM participant p, course c
WHERE p.email = 'alina.hug@example.com'
  AND c.title = 'Sommerkurs'
  AND NOT EXISTS (
    SELECT 1 FROM participant_course pc
    WHERE pc.participant_id = p.id AND pc.course_id = c.id
);

INSERT INTO participant_course (participant_id, course_id)
SELECT p.id, c.id
FROM participant p, course c
WHERE p.email = 'samuel.zimmermann@example.com'
  AND c.title = 'Sommerkurs'
  AND NOT EXISTS (
    SELECT 1 FROM participant_course pc
    WHERE pc.participant_id = p.id AND pc.course_id = c.id
);

INSERT INTO participant_course (participant_id, course_id)
SELECT p.id, c.id
FROM participant p, course c
WHERE p.email = 'chiara.egli@example.com'
  AND c.title = 'Sommerkurs'
  AND NOT EXISTS (
    SELECT 1 FROM participant_course pc
    WHERE pc.participant_id = p.id AND pc.course_id = c.id
);

INSERT INTO participant_course (participant_id, course_id)
SELECT p.id, c.id
FROM participant p, course c
WHERE p.email = 'david.suter@example.com'
  AND c.title = 'Sommerkurs'
  AND NOT EXISTS (
    SELECT 1 FROM participant_course pc
    WHERE pc.participant_id = p.id AND pc.course_id = c.id
);

INSERT INTO participant_course_level (participant_id, course_level_id)
SELECT p.id, cl.id
FROM participant p, course_level cl
WHERE p.email IN ('luca.weber@example.com', 'jonas.brunner@example.com', 'finn.huber@example.com', 'nico.roth@example.com')
  AND cl.code = 'A1'
  AND NOT EXISTS (
    SELECT 1 FROM participant_course_level pcl
    WHERE pcl.participant_id = p.id AND pcl.course_level_id = cl.id
);

INSERT INTO participant_course_level (participant_id, course_level_id)
SELECT p.id, cl.id
FROM participant p, course_level cl
WHERE p.email IN ('mia.keller@example.com', 'lina.schneider@example.com', 'emma.baumann@example.com', 'sofia.meier@example.com', 'elena.graf@example.com')
  AND cl.code = 'A2'
  AND NOT EXISTS (
    SELECT 1 FROM participant_course_level pcl
    WHERE pcl.participant_id = p.id AND pcl.course_level_id = cl.id
);

INSERT INTO participant_course_level (participant_id, course_level_id)
SELECT p.id, cl.id
FROM participant p, course_level cl
WHERE p.email IN ('leon.fischer@example.com', 'tim.bachmann@example.com', 'alina.hug@example.com', 'samuel.zimmermann@example.com', 'chiara.egli@example.com', 'david.suter@example.com')
  AND cl.code = 'B1'
  AND NOT EXISTS (
    SELECT 1 FROM participant_course_level pcl
    WHERE pcl.participant_id = p.id AND pcl.course_level_id = cl.id
);

