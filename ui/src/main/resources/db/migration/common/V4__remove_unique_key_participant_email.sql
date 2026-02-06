DO $$
BEGIN
  IF EXISTS (
    SELECT 1
    FROM pg_constraint
    WHERE conname = 'uk_participant_mandant_email'
      AND conrelid = 'public.participant'::regclass
  ) THEN
    ALTER TABLE public.participant
      DROP CONSTRAINT uk_participant_mandant_email;
  END IF;
END $$;