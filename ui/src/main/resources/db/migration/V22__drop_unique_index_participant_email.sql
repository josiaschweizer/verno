ALTER TABLE public.participant
DROP CONSTRAINT IF EXISTS uq_participant_email;

ALTER TABLE public.instructor
DROP CONSTRAINT IF EXISTS uq_instructor_email;