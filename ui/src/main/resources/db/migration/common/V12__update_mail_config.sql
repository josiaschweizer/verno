ALTER TABLE public.mail_config
    ADD COLUMN mail_validity VARCHAR(32);

UPDATE public.mail_config
SET mail_validity = CASE
    WHEN enabled = TRUE THEN 'TESTED_VALID'
    ELSE 'UNTESTED'
END;

ALTER TABLE public.mail_config
    ALTER COLUMN mail_validity SET DEFAULT 'UNTESTED';

ALTER TABLE public.mail_config
    ALTER COLUMN mail_validity SET NOT NULL;

ALTER TABLE public.mail_config
    DROP COLUMN enabled;