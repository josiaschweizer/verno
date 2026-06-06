ALTER TABLE public.text
    ADD COLUMN sub_identifier VARCHAR(255);

DROP INDEX IF EXISTS ux_text_mandant_identifier_language;

CREATE UNIQUE INDEX ux_text_mandant_identifier_sub_identifier_language
    ON public.text (
                    mandant_id,
                    identifier,
                    COALESCE(sub_identifier, ''),
                    language_code
        );