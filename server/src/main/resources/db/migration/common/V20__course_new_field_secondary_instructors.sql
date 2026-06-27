CREATE TABLE course_secondary_instructor
(
    course_id     BIGINT  NOT NULL,
    instructor_id BIGINT  NOT NULL,
    sort_index    INTEGER NOT NULL,

    CONSTRAINT pk_course_secondary_instructor
        PRIMARY KEY (course_id, sort_index),

    CONSTRAINT fk_course_secondary_instructor_course
        FOREIGN KEY (course_id)
            REFERENCES course (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_course_secondary_instructor_instructor
        FOREIGN KEY (instructor_id)
            REFERENCES instructor (id)
            ON DELETE CASCADE
);

CREATE UNIQUE INDEX uk_course_secondary_instructor_course_instructor
    ON course_secondary_instructor (course_id, instructor_id);