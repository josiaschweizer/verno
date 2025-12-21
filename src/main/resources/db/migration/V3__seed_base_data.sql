ALTER TABLE gender
    ALTER COLUMN name SET NOT NULL;

DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1
                       FROM pg_constraint
                       WHERE conname = 'uk_gender_name') THEN
            ALTER TABLE gender
                ADD CONSTRAINT uk_gender_name UNIQUE (name);
        END IF;
    END
$$;

ALTER TABLE day_of_week
    ALTER COLUMN name SET NOT NULL;

DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1
                       FROM pg_constraint
                       WHERE conname = 'uk_day_of_week_name') THEN
            ALTER TABLE day_of_week
                ADD CONSTRAINT uk_day_of_week_name UNIQUE (name);
        END IF;
    END
$$;

ALTER TABLE course_level
    ALTER COLUMN code SET NOT NULL;

DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1
                       FROM pg_constraint
                       WHERE conname = 'uk_course_level_code') THEN
            ALTER TABLE course_level
                ADD CONSTRAINT uk_course_level_code UNIQUE (code);
        END IF;
    END
$$;

INSERT INTO gender (name)
VALUES ('male'),
       ('female')
ON CONFLICT (name) DO NOTHING;

INSERT INTO day_of_week (name, sorting_number, active, name_de, name_en, name_fr)
VALUES ('MONDAY', 1, TRUE, 'Montag', 'Monday', 'Lundi'),
       ('TUESDAY', 2, TRUE, 'Dienstag', 'Tuesday', 'Mardi'),
       ('WEDNESDAY', 3, TRUE, 'Mittwoch', 'Wednesday', 'Mercredi'),
       ('THURSDAY', 4, TRUE, 'Donnerstag', 'Thursday', 'Jeudi'),
       ('FRIDAY', 5, TRUE, 'Freitag', 'Friday', 'Vendredi')
ON CONFLICT DO NOTHING;

INSERT INTO course_level (code, name, description, sorting_order)
VALUES ('BEGINNER', 'Anfänger', 'Einsteiger ohne Vorkenntnisse', 1),
       ('INTERMEDIATE', 'Fortgeschritten', 'Grundkenntnisse erforderlich', 2),
       ('ADVANCED', 'Experten', 'Sehr gute Kenntnisse erforderlich', 3)
ON CONFLICT DO NOTHING;