-- V20260203_01__add_mandant_id_everywhere.sql
-- PostgreSQL migration (Flyway). Assumption: bisher Single-Tenant Daten -> alles wird auf DEFAULT_MANDANT_ID gemappt.

BEGIN;

-- ------------------------------------------------------------
-- 0) Konfiguration: Default-Mandant (für bestehende Daten)
-- ------------------------------------------------------------
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='mandants') THEN
    CREATE TABLE public.mandants (
      id BIGINT PRIMARY KEY
    );
  END IF;
END $$;

DO $$
DECLARE
  DEFAULT_MANDANT_ID BIGINT := 1;
BEGIN
  IF NOT EXISTS (SELECT 1 FROM public.mandants WHERE id = DEFAULT_MANDANT_ID) THEN
    INSERT INTO public.mandants(id) VALUES (DEFAULT_MANDANT_ID);
  END IF;
END $$;

-- ------------------------------------------------------------
-- 1) Mandant Settings: auf "id == mandant_id" umstellen (MapsId)
-- ------------------------------------------------------------
DO $$
DECLARE
  DEFAULT_MANDANT_ID BIGINT := 1;
  cnt BIGINT;
BEGIN
  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='mandant_settings') THEN
    SELECT COUNT(*) INTO cnt FROM public.mandant_settings;
    IF cnt > 1 THEN
      RAISE EXCEPTION 'mandant_settings has % rows. Expected <= 1 for single-tenant migration.', cnt;
    END IF;

    -- Falls id noch identity/serial ist: wir setzen id auf DEFAULT_MANDANT_ID und erzwingen PK/FK
    UPDATE public.mandant_settings SET id = DEFAULT_MANDANT_ID;

    -- PK auf id sicherstellen
    IF EXISTS (
      SELECT 1 FROM pg_constraint
      WHERE conrelid = 'public.mandant_settings'::regclass
        AND contype = 'p'
    ) THEN
      -- ok
    ELSE
      ALTER TABLE public.mandant_settings ADD CONSTRAINT pk_mandant_settings PRIMARY KEY (id);
    END IF;

    -- FK auf mandants
    IF NOT EXISTS (
      SELECT 1 FROM pg_constraint
      WHERE conname = 'fk_mandant_settings_mandant'
    ) THEN
      ALTER TABLE public.mandant_settings
        ADD CONSTRAINT fk_mandant_settings_mandant
        FOREIGN KEY (id) REFERENCES public.mandants(id) ON DELETE CASCADE;
    END IF;
  END IF;
END $$;

-- ------------------------------------------------------------
-- 2) Helper: mandant_id Spalte hinzufügen (falls nicht vorhanden)
-- ------------------------------------------------------------
DO $$
BEGIN
  -- Tabellen, die mandantenfähig werden sollen:
  -- address, instructor, parent, participant, course_schedule, course, app_user, app_user_settings, course_level

  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='address')
     AND NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='address' AND column_name='mandant_id') THEN
    ALTER TABLE public.address ADD COLUMN mandant_id BIGINT;
  END IF;

  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='instructor')
     AND NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='instructor' AND column_name='mandant_id') THEN
    ALTER TABLE public.instructor ADD COLUMN mandant_id BIGINT;
  END IF;

  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='parent')
     AND NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='parent' AND column_name='mandant_id') THEN
    ALTER TABLE public.parent ADD COLUMN mandant_id BIGINT;
  END IF;

  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='participant')
     AND NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='participant' AND column_name='mandant_id') THEN
    ALTER TABLE public.participant ADD COLUMN mandant_id BIGINT;
  END IF;

  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='course_schedule')
     AND NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='course_schedule' AND column_name='mandant_id') THEN
    ALTER TABLE public.course_schedule ADD COLUMN mandant_id BIGINT;
  END IF;

  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='course')
     AND NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='course' AND column_name='mandant_id') THEN
    ALTER TABLE public.course ADD COLUMN mandant_id BIGINT;
  END IF;

  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='app_user')
     AND NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='app_user' AND column_name='mandant_id') THEN
    ALTER TABLE public.app_user ADD COLUMN mandant_id BIGINT;
  END IF;

  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='app_user_settings')
     AND NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='app_user_settings' AND column_name='mandant_id') THEN
    ALTER TABLE public.app_user_settings ADD COLUMN mandant_id BIGINT;
  END IF;

  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='course_level')
     AND NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='course_level' AND column_name='mandant_id') THEN
    ALTER TABLE public.course_level ADD COLUMN mandant_id BIGINT;
  END IF;
END $$;

-- ------------------------------------------------------------
-- 3) Backfill mandant_id (Single-Tenant -> alles = 1)
-- ------------------------------------------------------------
DO $$
DECLARE
  DEFAULT_MANDANT_ID BIGINT := 1;
BEGIN
  IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='address' AND column_name='mandant_id') THEN
    UPDATE public.address SET mandant_id = DEFAULT_MANDANT_ID WHERE mandant_id IS NULL;
  END IF;

  IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='instructor' AND column_name='mandant_id') THEN
    UPDATE public.instructor SET mandant_id = DEFAULT_MANDANT_ID WHERE mandant_id IS NULL;
  END IF;

  IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='parent' AND column_name='mandant_id') THEN
    UPDATE public.parent SET mandant_id = DEFAULT_MANDANT_ID WHERE mandant_id IS NULL;
  END IF;

  IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='participant' AND column_name='mandant_id') THEN
    UPDATE public.participant SET mandant_id = DEFAULT_MANDANT_ID WHERE mandant_id IS NULL;
  END IF;

  IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='course_schedule' AND column_name='mandant_id') THEN
    UPDATE public.course_schedule SET mandant_id = DEFAULT_MANDANT_ID WHERE mandant_id IS NULL;
  END IF;

  IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='course' AND column_name='mandant_id') THEN
    UPDATE public.course SET mandant_id = DEFAULT_MANDANT_ID WHERE mandant_id IS NULL;
  END IF;

  IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='app_user' AND column_name='mandant_id') THEN
    UPDATE public.app_user SET mandant_id = DEFAULT_MANDANT_ID WHERE mandant_id IS NULL;
  END IF;

  -- app_user_settings: prefer mandant_id via user join (falls schon da), sonst default
  IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='app_user_settings' AND column_name='mandant_id') THEN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='app_user_settings' AND column_name='user_id')
       AND EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='app_user' AND column_name='mandant_id') THEN
      UPDATE public.app_user_settings s
         SET mandant_id = u.mandant_id
        FROM public.app_user u
       WHERE s.mandant_id IS NULL
         AND s.user_id = u.id;
    END IF;

    UPDATE public.app_user_settings SET mandant_id = DEFAULT_MANDANT_ID WHERE mandant_id IS NULL;
  END IF;

  IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='course_level' AND column_name='mandant_id') THEN
    UPDATE public.course_level SET mandant_id = DEFAULT_MANDANT_ID WHERE mandant_id IS NULL;
  END IF;
END $$;

-- ------------------------------------------------------------
-- 4) NOT NULL + FK Constraints
-- ------------------------------------------------------------
DO $$
BEGIN
  -- address
  IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='address' AND column_name='mandant_id') THEN
    ALTER TABLE public.address ALTER COLUMN mandant_id SET NOT NULL;

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname='fk_address_mandant') THEN
      ALTER TABLE public.address
        ADD CONSTRAINT fk_address_mandant
        FOREIGN KEY (mandant_id) REFERENCES public.mandants(id) ON DELETE RESTRICT;
    END IF;
  END IF;

  -- instructor
  IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='instructor' AND column_name='mandant_id') THEN
    ALTER TABLE public.instructor ALTER COLUMN mandant_id SET NOT NULL;

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname='fk_instructor_mandant') THEN
      ALTER TABLE public.instructor
        ADD CONSTRAINT fk_instructor_mandant
        FOREIGN KEY (mandant_id) REFERENCES public.mandants(id) ON DELETE RESTRICT;
    END IF;
  END IF;

  -- parent
  IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='parent' AND column_name='mandant_id') THEN
    ALTER TABLE public.parent ALTER COLUMN mandant_id SET NOT NULL;

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname='fk_parent_mandant') THEN
      ALTER TABLE public.parent
        ADD CONSTRAINT fk_parent_mandant
        FOREIGN KEY (mandant_id) REFERENCES public.mandants(id) ON DELETE RESTRICT;
    END IF;
  END IF;

  -- participant
  IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='participant' AND column_name='mandant_id') THEN
    ALTER TABLE public.participant ALTER COLUMN mandant_id SET NOT NULL;

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname='fk_participant_mandant') THEN
      ALTER TABLE public.participant
        ADD CONSTRAINT fk_participant_mandant
        FOREIGN KEY (mandant_id) REFERENCES public.mandants(id) ON DELETE RESTRICT;
    END IF;
  END IF;

  -- course_schedule
  IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='course_schedule' AND column_name='mandant_id') THEN
    ALTER TABLE public.course_schedule ALTER COLUMN mandant_id SET NOT NULL;

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname='fk_course_schedule_mandant') THEN
      ALTER TABLE public.course_schedule
        ADD CONSTRAINT fk_course_schedule_mandant
        FOREIGN KEY (mandant_id) REFERENCES public.mandants(id) ON DELETE RESTRICT;
    END IF;
  END IF;

  -- course
  IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='course' AND column_name='mandant_id') THEN
    ALTER TABLE public.course ALTER COLUMN mandant_id SET NOT NULL;

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname='fk_course_mandant') THEN
      ALTER TABLE public.course
        ADD CONSTRAINT fk_course_mandant
        FOREIGN KEY (mandant_id) REFERENCES public.mandants(id) ON DELETE RESTRICT;
    END IF;
  END IF;

  -- app_user
  IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='app_user' AND column_name='mandant_id') THEN
    ALTER TABLE public.app_user ALTER COLUMN mandant_id SET NOT NULL;

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname='fk_app_user_mandant') THEN
      ALTER TABLE public.app_user
        ADD CONSTRAINT fk_app_user_mandant
        FOREIGN KEY (mandant_id) REFERENCES public.mandants(id) ON DELETE RESTRICT;
    END IF;
  END IF;

  -- app_user_settings
  IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='app_user_settings' AND column_name='mandant_id') THEN
    ALTER TABLE public.app_user_settings ALTER COLUMN mandant_id SET NOT NULL;

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname='fk_app_user_settings_mandant') THEN
      ALTER TABLE public.app_user_settings
        ADD CONSTRAINT fk_app_user_settings_mandant
        FOREIGN KEY (mandant_id) REFERENCES public.mandants(id) ON DELETE RESTRICT;
    END IF;
  END IF;

  -- course_level
  IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='course_level' AND column_name='mandant_id') THEN
    ALTER TABLE public.course_level ALTER COLUMN mandant_id SET NOT NULL;

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname='fk_course_level_mandant') THEN
      ALTER TABLE public.course_level
        ADD CONSTRAINT fk_course_level_mandant
        FOREIGN KEY (mandant_id) REFERENCES public.mandants(id) ON DELETE RESTRICT;
    END IF;
  END IF;
END $$;

-- ------------------------------------------------------------
-- 5) Unique Constraints (pro Mandant) + Indizes (mandant_id first)
-- ------------------------------------------------------------

-- app_user: username unique pro Mandant (alte globale Unique-Constraint ggf. manuell entfernen, wenn Name unbekannt)
DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='app_user') THEN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname='uk_app_user_mandant_username') THEN
      ALTER TABLE public.app_user
        ADD CONSTRAINT uk_app_user_mandant_username UNIQUE (mandant_id, username);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE schemaname='public' AND indexname='ix_app_user_mandant') THEN
      CREATE INDEX ix_app_user_mandant ON public.app_user (mandant_id);
    END IF;
  END IF;
END $$;

-- app_user_settings
DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='app_user_settings') THEN
    IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE schemaname='public' AND indexname='ix_app_user_settings_mandant') THEN
      CREATE INDEX ix_app_user_settings_mandant ON public.app_user_settings (mandant_id);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE schemaname='public' AND indexname='ix_app_user_settings_user') THEN
      CREATE INDEX ix_app_user_settings_user ON public.app_user_settings (user_id);
    END IF;
  END IF;
END $$;

-- course_level: code unique pro Mandant
DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='course_level') THEN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname='uk_course_level_mandant_code') THEN
      ALTER TABLE public.course_level
        ADD CONSTRAINT uk_course_level_mandant_code UNIQUE (mandant_id, code);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE schemaname='public' AND indexname='ix_course_level_mandant') THEN
      CREATE INDEX ix_course_level_mandant ON public.course_level (mandant_id);
    END IF;
  END IF;
END $$;

-- core tables indexes
DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='address')
     AND NOT EXISTS (SELECT 1 FROM pg_indexes WHERE schemaname='public' AND indexname='ix_address_mandant') THEN
    CREATE INDEX ix_address_mandant ON public.address (mandant_id);
  END IF;

  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='instructor')
     AND NOT EXISTS (SELECT 1 FROM pg_indexes WHERE schemaname='public' AND indexname='ix_instructor_mandant') THEN
    CREATE INDEX ix_instructor_mandant ON public.instructor (mandant_id);
  END IF;

  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='parent')
     AND NOT EXISTS (SELECT 1 FROM pg_indexes WHERE schemaname='public' AND indexname='ix_parent_mandant') THEN
    CREATE INDEX ix_parent_mandant ON public.parent (mandant_id);
  END IF;

  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='participant')
     AND NOT EXISTS (SELECT 1 FROM pg_indexes WHERE schemaname='public' AND indexname='ix_participant_mandant') THEN
    CREATE INDEX ix_participant_mandant ON public.participant (mandant_id);
  END IF;

  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='course_schedule')
     AND NOT EXISTS (SELECT 1 FROM pg_indexes WHERE schemaname='public' AND indexname='ix_course_schedule_mandant') THEN
    CREATE INDEX ix_course_schedule_mandant ON public.course_schedule (mandant_id);
  END IF;

  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='course')
     AND NOT EXISTS (SELECT 1 FROM pg_indexes WHERE schemaname='public' AND indexname='ix_course_mandant') THEN
    CREATE INDEX ix_course_mandant ON public.course (mandant_id);
  END IF;
END $$;

-- ------------------------------------------------------------
-- 6) Join Tables mandant-sicher machen (mandant_id + indexes + uniqueness)
-- ------------------------------------------------------------

-- helper: add mandant_id columns
DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='course_course_level')
     AND NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='course_course_level' AND column_name='mandant_id') THEN
    ALTER TABLE public.course_course_level ADD COLUMN mandant_id BIGINT;
  END IF;

  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='participant_course_level')
     AND NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='participant_course_level' AND column_name='mandant_id') THEN
    ALTER TABLE public.participant_course_level ADD COLUMN mandant_id BIGINT;
  END IF;

  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='participant_course')
     AND NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='participant_course' AND column_name='mandant_id') THEN
    ALTER TABLE public.participant_course ADD COLUMN mandant_id BIGINT;
  END IF;

  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='course_weekday')
     AND NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='course_weekday' AND column_name='mandant_id') THEN
    ALTER TABLE public.course_weekday ADD COLUMN mandant_id BIGINT;
  END IF;

  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='course_schedule_week')
     AND NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='course_schedule_week' AND column_name='mandant_id') THEN
    ALTER TABLE public.course_schedule_week ADD COLUMN mandant_id BIGINT;
  END IF;
END $$;

-- backfill join tables using parent tables' mandant_id
DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='course_course_level') THEN
    UPDATE public.course_course_level j
       SET mandant_id = c.mandant_id
      FROM public.course c
     WHERE j.mandant_id IS NULL
       AND j.course_id = c.id;
  END IF;

  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='participant_course_level') THEN
    UPDATE public.participant_course_level j
       SET mandant_id = p.mandant_id
      FROM public.participant p
     WHERE j.mandant_id IS NULL
       AND j.participant_id = p.id;
  END IF;

  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='participant_course') THEN
    UPDATE public.participant_course j
       SET mandant_id = p.mandant_id
      FROM public.participant p
     WHERE j.mandant_id IS NULL
       AND j.participant_id = p.id;
  END IF;

  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='course_weekday') THEN
    UPDATE public.course_weekday j
       SET mandant_id = c.mandant_id
      FROM public.course c
     WHERE j.mandant_id IS NULL
       AND j.course_id = c.id;
  END IF;

  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='course_schedule_week') THEN
    UPDATE public.course_schedule_week j
       SET mandant_id = cs.mandant_id
      FROM public.course_schedule cs
     WHERE j.mandant_id IS NULL
       AND j.course_schedule_id = cs.id;
  END IF;
END $$;

-- join tables NOT NULL + FK + uniqueness + indexes
DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='course_course_level') THEN
    ALTER TABLE public.course_course_level ALTER COLUMN mandant_id SET NOT NULL;

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname='fk_course_course_level_mandant') THEN
      ALTER TABLE public.course_course_level
        ADD CONSTRAINT fk_course_course_level_mandant
        FOREIGN KEY (mandant_id) REFERENCES public.mandants(id) ON DELETE RESTRICT;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname='uk_course_course_level_mandant') THEN
      ALTER TABLE public.course_course_level
        ADD CONSTRAINT uk_course_course_level_mandant UNIQUE (mandant_id, course_id, course_level_id);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE schemaname='public' AND indexname='ix_course_course_level_mandant') THEN
      CREATE INDEX ix_course_course_level_mandant ON public.course_course_level (mandant_id);
    END IF;
  END IF;

  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='participant_course_level') THEN
    ALTER TABLE public.participant_course_level ALTER COLUMN mandant_id SET NOT NULL;

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname='fk_participant_course_level_mandant') THEN
      ALTER TABLE public.participant_course_level
        ADD CONSTRAINT fk_participant_course_level_mandant
        FOREIGN KEY (mandant_id) REFERENCES public.mandants(id) ON DELETE RESTRICT;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname='uk_participant_course_level_mandant') THEN
      ALTER TABLE public.participant_course_level
        ADD CONSTRAINT uk_participant_course_level_mandant UNIQUE (mandant_id, participant_id, course_level_id);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE schemaname='public' AND indexname='ix_participant_course_level_mandant') THEN
      CREATE INDEX ix_participant_course_level_mandant ON public.participant_course_level (mandant_id);
    END IF;
  END IF;

  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='participant_course') THEN
    ALTER TABLE public.participant_course ALTER COLUMN mandant_id SET NOT NULL;

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname='fk_participant_course_mandant') THEN
      ALTER TABLE public.participant_course
        ADD CONSTRAINT fk_participant_course_mandant
        FOREIGN KEY (mandant_id) REFERENCES public.mandants(id) ON DELETE RESTRICT;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname='uk_participant_course_mandant') THEN
      ALTER TABLE public.participant_course
        ADD CONSTRAINT uk_participant_course_mandant UNIQUE (mandant_id, participant_id, course_id);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE schemaname='public' AND indexname='ix_participant_course_mandant') THEN
      CREATE INDEX ix_participant_course_mandant ON public.participant_course (mandant_id);
    END IF;
  END IF;

  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='course_weekday') THEN
    ALTER TABLE public.course_weekday ALTER COLUMN mandant_id SET NOT NULL;

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname='fk_course_weekday_mandant') THEN
      ALTER TABLE public.course_weekday
        ADD CONSTRAINT fk_course_weekday_mandant
        FOREIGN KEY (mandant_id) REFERENCES public.mandants(id) ON DELETE RESTRICT;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname='uk_course_weekday_mandant') THEN
      ALTER TABLE public.course_weekday
        ADD CONSTRAINT uk_course_weekday_mandant UNIQUE (mandant_id, course_id, sort_index);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE schemaname='public' AND indexname='ix_course_weekday_mandant') THEN
      CREATE INDEX ix_course_weekday_mandant ON public.course_weekday (mandant_id);
    END IF;
  END IF;

  IF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname='public' AND tablename='course_schedule_week') THEN
    ALTER TABLE public.course_schedule_week ALTER COLUMN mandant_id SET NOT NULL;

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname='fk_course_schedule_week_mandant') THEN
      ALTER TABLE public.course_schedule_week
        ADD CONSTRAINT fk_course_schedule_week_mandant
        FOREIGN KEY (mandant_id) REFERENCES public.mandants(id) ON DELETE RESTRICT;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname='uk_course_schedule_week_mandant') THEN
      ALTER TABLE public.course_schedule_week
        ADD CONSTRAINT uk_course_schedule_week_mandant UNIQUE (mandant_id, course_schedule_id, sort_index);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE schemaname='public' AND indexname='ix_course_schedule_week_mandant') THEN
      CREATE INDEX ix_course_schedule_week_mandant ON public.course_schedule_week (mandant_id);
    END IF;
  END IF;
END $$;

COMMIT;