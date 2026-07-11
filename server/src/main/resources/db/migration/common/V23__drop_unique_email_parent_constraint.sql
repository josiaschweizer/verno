ALTER TABLE public.parent
    DROP CONSTRAINT IF EXISTS uk_parent_mandant_email;

DROP INDEX IF EXISTS public.uk_parent_mandant_email;