CREATE TABLE public.text
(
    id            BIGSERIAL PRIMARY KEY,
    mandant_id    BIGINT       NOT NULL,
    identifier    VARCHAR(255) NOT NULL,
    language_code VARCHAR(10)  NOT NULL,
    text_value    TEXT         NOT NULL,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX ux_text_mandant_identifier_language
    ON public.text (
                    mandant_id,
                    identifier,
                    language_code
        );