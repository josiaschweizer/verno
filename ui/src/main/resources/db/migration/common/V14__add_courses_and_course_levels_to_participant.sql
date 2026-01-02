-- V14__add_courses_and_course_levels_to_participant.sql

BEGIN;

-- 1) Join table: participant ↔ course
CREATE TABLE IF NOT EXISTS participant_course
(
    participant_id BIGINT NOT NULL,
    course_id      BIGINT NOT NULL,
    PRIMARY KEY (participant_id, course_id),
    CONSTRAINT fk_participant_course_participant
    FOREIGN KEY (participant_id) REFERENCES participant (id) ON DELETE CASCADE,
    CONSTRAINT fk_participant_course_course
    FOREIGN KEY (course_id) REFERENCES course (id)
    );

CREATE INDEX IF NOT EXISTS ix_participant_course_participant_id
    ON participant_course (participant_id);

CREATE INDEX IF NOT EXISTS ix_participant_course_course_id
    ON participant_course (course_id);

-- 2) Join table: participant ↔ course_level
CREATE TABLE IF NOT EXISTS participant_course_level
(
    participant_id  BIGINT NOT NULL,
    course_level_id BIGINT NOT NULL,
    PRIMARY KEY (participant_id, course_level_id),
    CONSTRAINT fk_participant_course_level_participant
    FOREIGN KEY (participant_id) REFERENCES participant (id) ON DELETE CASCADE,
    CONSTRAINT fk_participant_course_level_course_level
    FOREIGN KEY (course_level_id) REFERENCES course_level (id)
    );

CREATE INDEX IF NOT EXISTS ix_participant_course_level_participant_id
    ON participant_course_level (participant_id);

CREATE INDEX IF NOT EXISTS ix_participant_course_level_course_level_id
    ON participant_course_level (course_level_id);

-- 3) Backfill from old columns (single value) if they existed
-- participant.course -> participant_course
INSERT INTO participant_course (participant_id, course_id)
SELECT p.id, p.course
FROM participant p
WHERE p.course IS NOT NULL
  AND NOT EXISTS (
    SELECT 1
    FROM participant_course pc
    WHERE pc.participant_id = p.id
      AND pc.course_id = p.course
);

-- participant.course_level -> participant_course_level
INSERT INTO participant_course_level (participant_id, course_level_id)
SELECT p.id, p.course_level
FROM participant p
WHERE p.course_level IS NOT NULL
  AND NOT EXISTS (
    SELECT 1
    FROM participant_course_level pcl
    WHERE pcl.participant_id = p.id
      AND pcl.course_level_id = p.course_level
);

-- 4) Drop old columns (now replaced by join tables)
ALTER TABLE participant
DROP COLUMN IF EXISTS course;

ALTER TABLE participant
DROP COLUMN IF EXISTS course_level;

COMMIT;