INSERT INTO public.gender (created_at, name, description)
SELECT CURRENT_TIMESTAMP, 'Male', 'Männlich'
    WHERE NOT EXISTS (SELECT 1 FROM public.gender WHERE name = 'Male');

INSERT INTO public.gender (created_at, name, description)
SELECT CURRENT_TIMESTAMP, 'Female', 'Weiblich'
    WHERE NOT EXISTS (SELECT 1 FROM public.gender WHERE name = 'Female');