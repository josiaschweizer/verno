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
             ('Albisriederstrasse', '333', '8047', 'Zürich',
              'Schweiz')) AS v(street, house_number, zip_code, city, country)
WHERE NOT EXISTS (SELECT 1
                  FROM address a
                  WHERE a.street = v.street
                    AND a.house_number = v.house_number
                    AND a.zip_code = v.zip_code
                    AND a.city = v.city
                    AND a.country = v.country);

UPDATE participant p
SET address = (SELECT a.id
               FROM address a
               WHERE a.street = 'Seestrasse'
                 AND a.house_number = '12'
                 AND a.zip_code = '8002'
                 AND a.city = 'Zürich'
                 AND a.country = 'Schweiz')
WHERE p.email IN ('tim.mueller@family.example', 'lina.mueller@family.example')
  AND p.address IS NULL;

UPDATE participant p
SET address = (SELECT a.id
               FROM address a
               WHERE a.street = 'Bahnhofstrasse'
                 AND a.house_number = '7'
                 AND a.zip_code = '8001'
                 AND a.city = 'Zürich'
                 AND a.country = 'Schweiz')
WHERE p.email IN ('noel.steiner@family.example', 'mara.steiner@family.example')
  AND p.address IS NULL;

UPDATE participant p
SET address = (SELECT a.id
               FROM address a
               WHERE a.street = 'Limmatquai'
                 AND a.house_number = '3'
                 AND a.zip_code = '8001'
                 AND a.city = 'Zürich'
                 AND a.country = 'Schweiz')
WHERE p.email IN ('elias.hug@family.example', 'sara.hug@family.example')
  AND p.address IS NULL;

UPDATE participant p
SET address = (SELECT a.id
               FROM address a
               WHERE a.street = 'Uetlibergstrasse'
                 AND a.house_number = '55'
                 AND a.zip_code = '8045'
                 AND a.city = 'Zürich'
                 AND a.country = 'Schweiz')
WHERE p.email IN ('leon.bachmann@family.example', 'nina.bachmann@family.example')
  AND p.address IS NULL;

UPDATE participant p
SET address = (SELECT a.id
               FROM address a
               WHERE a.street = 'Dübendorfstrasse'
                 AND a.house_number = '101'
                 AND a.zip_code = '8051'
                 AND a.city = 'Zürich'
                 AND a.country = 'Schweiz')
WHERE p.email IN ('paul.arnold@family.example', 'alina.arnold@family.example')
  AND p.address IS NULL;

UPDATE instructor i
SET address = (SELECT a.id
               FROM address a
               WHERE a.street = 'Winterthurerstrasse'
                 AND a.house_number = '88'
                 AND a.zip_code = '8057'
                 AND a.city = 'Zürich'
                 AND a.country = 'Schweiz')
WHERE i.email IN ('lena.meier@swimschool.example', 'ben.graf@swimschool.example')
  AND i.address IS NULL;

UPDATE instructor i
SET address = (SELECT a.id
               FROM address a
               WHERE a.street = 'Badenerstrasse'
                 AND a.house_number = '210'
                 AND a.zip_code = '8004'
                 AND a.city = 'Zürich'
                 AND a.country = 'Schweiz')
WHERE i.email IN ('noah.keller@swimschool.example', 'sofia.baumann@swimschool.example')
  AND i.address IS NULL;

UPDATE instructor i
SET address = (SELECT a.id
               FROM address a
               WHERE a.street = 'Birmensdorferstrasse'
                 AND a.house_number = '15'
                 AND a.zip_code = '8003'
                 AND a.city = 'Zürich'
                 AND a.country = 'Schweiz')
WHERE i.email IN ('mia.schmid@swimschool.example', 'finn.huber@swimschool.example')
  AND i.address IS NULL;

UPDATE instructor i
SET address = (SELECT a.id
               FROM address a
               WHERE a.street = 'Zürichbergstrasse'
                 AND a.house_number = '25'
                 AND a.zip_code = '8044'
                 AND a.city = 'Zürich'
                 AND a.country = 'Schweiz')
WHERE i.email IN ('luca.frei@swimschool.example', 'lea.kunz@swimschool.example')
  AND i.address IS NULL;

UPDATE instructor i
SET address = (SELECT a.id
               FROM address a
               WHERE a.street = 'Albisriederstrasse'
                 AND a.house_number = '333'
                 AND a.zip_code = '8047'
                 AND a.city = 'Zürich'
                 AND a.country = 'Schweiz')
WHERE i.email IN ('emma.weber@swimschool.example', 'jonas.roth@swimschool.example')
  AND i.address IS NULL;

INSERT INTO parent (created_at, firstname, lastname, email, phone, gender, address)
SELECT NOW(), v.firstname, v.lastname, v.email, v.phone, v.gender_id, v.address_id
FROM (VALUES ('Markus', 'Müller', 'markus.mueller@family.example', '+41 76 301 01 01',
              (SELECT id FROM gender WHERE name = 'Male'),
              (SELECT a.id
               FROM address a
               WHERE a.street = 'Seestrasse'
                 AND a.house_number = '12'
                 AND a.zip_code = '8002'
                 AND a.city = 'Zürich'
                 AND a.country = 'Schweiz')),
             ('Sabine', 'Müller', 'sabine.mueller@family.example', '+41 76 301 01 02',
              (SELECT id FROM gender WHERE name = 'Female'),
              (SELECT a.id
               FROM address a
               WHERE a.street = 'Seestrasse'
                 AND a.house_number = '12'
                 AND a.zip_code = '8002'
                 AND a.city = 'Zürich'
                 AND a.country = 'Schweiz')),
             ('Daniel', 'Steiner', 'daniel.steiner@family.example', '+41 76 302 02 01',
              (SELECT id FROM gender WHERE name = 'Male'),
              (SELECT a.id
               FROM address a
               WHERE a.street = 'Bahnhofstrasse'
                 AND a.house_number = '7'
                 AND a.zip_code = '8001'
                 AND a.city = 'Zürich'
                 AND a.country = 'Schweiz')),
             ('Nathalie', 'Steiner', 'nathalie.steiner@family.example', '+41 76 302 02 02',
              (SELECT id FROM gender WHERE name = 'Female'),
              (SELECT a.id
               FROM address a
               WHERE a.street = 'Bahnhofstrasse'
                 AND a.house_number = '7'
                 AND a.zip_code = '8001'
                 AND a.city = 'Zürich'
                 AND a.country = 'Schweiz')),
             ('Patrick', 'Hug', 'patrick.hug@family.example', '+41 76 303 03 01',
              (SELECT id FROM gender WHERE name = 'Male'),
              (SELECT a.id
               FROM address a
               WHERE a.street = 'Limmatquai'
                 AND a.house_number = '3'
                 AND a.zip_code = '8001'
                 AND a.city = 'Zürich'
                 AND a.country = 'Schweiz')),
             ('Julia', 'Hug', 'julia.hug@family.example', '+41 76 303 03 02',
              (SELECT id FROM gender WHERE name = 'Female'),
              (SELECT a.id
               FROM address a
               WHERE a.street = 'Limmatquai'
                 AND a.house_number = '3'
                 AND a.zip_code = '8001'
                 AND a.city = 'Zürich'
                 AND a.country = 'Schweiz')),
             ('Michael', 'Bachmann', 'michael.bachmann@family.example', '+41 76 304 04 01',
              (SELECT id FROM gender WHERE name = 'Male'),
              (SELECT a.id
               FROM address a
               WHERE a.street = 'Uetlibergstrasse'
                 AND a.house_number = '55'
                 AND a.zip_code = '8045'
                 AND a.city = 'Zürich'
                 AND a.country = 'Schweiz')),
             ('Carla', 'Bachmann', 'carla.bachmann@family.example', '+41 76 304 04 02',
              (SELECT id FROM gender WHERE name = 'Female'),
              (SELECT a.id
               FROM address a
               WHERE a.street = 'Uetlibergstrasse'
                 AND a.house_number = '55'
                 AND a.zip_code = '8045'
                 AND a.city = 'Zürich'
                 AND a.country = 'Schweiz')),
             ('Thomas', 'Arnold', 'thomas.arnold@family.example', '+41 76 305 05 01',
              (SELECT id FROM gender WHERE name = 'Male'),
              (SELECT a.id
               FROM address a
               WHERE a.street = 'Dübendorfstrasse'
                 AND a.house_number = '101'
                 AND a.zip_code = '8051'
                 AND a.city = 'Zürich'
                 AND a.country = 'Schweiz')),
             ('Elena', 'Arnold', 'elena.arnold@family.example', '+41 76 305 05 02',
              (SELECT id FROM gender WHERE name = 'Female'),
              (SELECT a.id
               FROM address a
               WHERE a.street = 'Dübendorfstrasse'
                 AND a.house_number = '101'
                 AND a.zip_code = '8051'
                 AND a.city = 'Zürich'
                 AND a.country = 'Schweiz'))) AS v(firstname, lastname, email, phone, gender_id, address_id)
WHERE NOT EXISTS (SELECT 1 FROM parent p WHERE p.email = v.email);

UPDATE participant p
SET parent_one = (SELECT id FROM parent WHERE email = 'markus.mueller@family.example'),
    parent_two = (SELECT id FROM parent WHERE email = 'sabine.mueller@family.example')
WHERE p.email IN ('tim.mueller@family.example', 'lina.mueller@family.example')
  AND (p.parent_one IS NULL AND p.parent_two IS NULL);

UPDATE participant p
SET parent_one = (SELECT id FROM parent WHERE email = 'daniel.steiner@family.example'),
    parent_two = (SELECT id FROM parent WHERE email = 'nathalie.steiner@family.example')
WHERE p.email IN ('noel.steiner@family.example', 'mara.steiner@family.example')
  AND (p.parent_one IS NULL AND p.parent_two IS NULL);

UPDATE participant p
SET parent_one = (SELECT id FROM parent WHERE email = 'patrick.hug@family.example'),
    parent_two = (SELECT id FROM parent WHERE email = 'julia.hug@family.example')
WHERE p.email IN ('elias.hug@family.example', 'sara.hug@family.example')
  AND (p.parent_one IS NULL AND p.parent_two IS NULL);

UPDATE participant p
SET parent_one = (SELECT id FROM parent WHERE email = 'michael.bachmann@family.example'),
    parent_two = (SELECT id FROM parent WHERE email = 'carla.bachmann@family.example')
WHERE p.email IN ('leon.bachmann@family.example', 'nina.bachmann@family.example')
  AND (p.parent_one IS NULL AND p.parent_two IS NULL);

UPDATE participant p
SET parent_one = (SELECT id FROM parent WHERE email = 'thomas.arnold@family.example'),
    parent_two = (SELECT id FROM parent WHERE email = 'elena.arnold@family.example')
WHERE p.email IN ('paul.arnold@family.example', 'alina.arnold@family.example')
  AND (p.parent_one IS NULL AND p.parent_two IS NULL);