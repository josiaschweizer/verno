INSERT INTO participant (created_at,
                         firstname,
                         lastname,
                         birthdate,
                         gender,
                         course_level,
                         address,
                         email,
                         phone,
                         active,
                         note)
SELECT CURRENT_TIMESTAMP,
       'Lena',
       'Meier',
       DATE '2011-03-12',
       (SELECT id FROM gender WHERE name = 'Female' LIMIT 1),
       (SELECT id FROM course_level WHERE code = 'A1' LIMIT 1),
       (SELECT id
        FROM address
        WHERE street = 'Musterstrasse'
          AND house_number = '1'
          AND zip_code = '8000'
          AND city = 'Zürich'
          AND country = 'Schweiz'
        LIMIT 1),
       'lena.meier@example.com',
       '+41791234568',
       FALSE,
       'seed: inactive participant'
WHERE NOT EXISTS (SELECT 1
                  FROM participant
                  WHERE email = 'lena.meier@example.com');