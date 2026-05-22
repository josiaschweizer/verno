ALTER TABLE gender
    ADD COLUMN IF NOT EXISTS mandant_id BIGINT;

ALTER TABLE gender
    DROP CONSTRAINT IF EXISTS uq_gender_name;

DROP INDEX IF EXISTS uq_gender_name;

DO $$
    DECLARE
        tenant_record RECORD;
        new_male_id BIGINT;
        new_female_id BIGINT;
    BEGIN
        FOR tenant_record IN
            SELECT DISTINCT mandant_id
            FROM (
                     SELECT mandant_id FROM participant WHERE mandant_id IS NOT NULL
                     UNION
                     SELECT mandant_id FROM parent WHERE mandant_id IS NOT NULL
                     UNION
                     SELECT mandant_id FROM instructor WHERE mandant_id IS NOT NULL
                 ) tenants
            LOOP
                INSERT INTO gender (created_at, mandant_id, name, description)
                VALUES (NOW(), tenant_record.mandant_id, 'Male', 'Männlich')
                RETURNING id INTO new_male_id;

                INSERT INTO gender (created_at, mandant_id, name, description)
                VALUES (NOW(), tenant_record.mandant_id, 'Female', 'Weiblich')
                RETURNING id INTO new_female_id;

                UPDATE participant
                SET gender = CASE
                                    WHEN gender = 1 THEN new_male_id
                                    WHEN gender = 2 THEN new_female_id
                                    ELSE gender
                    END
                WHERE mandant_id = tenant_record.mandant_id
                  AND gender IN (1, 2);

                UPDATE parent
                SET gender = CASE
                                    WHEN gender = 1 THEN new_male_id
                                    WHEN gender = 2 THEN new_female_id
                                    ELSE gender
                    END
                WHERE mandant_id = tenant_record.mandant_id
                  AND gender IN (1, 2);

                UPDATE instructor
                SET gender = CASE
                                    WHEN gender = 1 THEN new_male_id
                                    WHEN gender = 2 THEN new_female_id
                                    ELSE gender
                    END
                WHERE mandant_id = tenant_record.mandant_id
                  AND gender IN (1, 2);
            END LOOP;
    END $$;

DELETE FROM gender
WHERE id IN (1, 2);

ALTER TABLE gender
    ALTER COLUMN mandant_id SET NOT NULL;

CREATE UNIQUE INDEX IF NOT EXISTS ux_gender_mandant_name
    ON gender (mandant_id, name);