ALTER TABLE file_store ALTER COLUMN storage_key DROP NOT NULL;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'file_store' AND column_name = 'mandant_id'
    ) THEN
        ALTER TABLE file_store ADD COLUMN mandant_id bigint;

        UPDATE file_store SET mandant_id = 7777 WHERE mandant_id IS NULL;

        ALTER TABLE file_store ALTER COLUMN mandant_id SET NOT NULL;
        ALTER TABLE file_store ADD CONSTRAINT fk_file_store_mandant
            FOREIGN KEY (mandant_id) REFERENCES mandants(id) ON DELETE CASCADE;

        CREATE INDEX ix_file_store_mandant ON file_store(mandant_id);
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'file_store' AND column_name = 'id' AND data_type = 'character varying'
    ) THEN
        DROP TABLE IF EXISTS file_store CASCADE;

        CREATE TABLE file_store (
            id bigserial primary key,
            filename varchar(512) not null,
            content_type varchar(128) not null,
            size bigint not null,
            checksum_sha256 varchar(128) not null,
            storage_key varchar(1024),
            created_at timestamptz not null,
            mandant_id bigint not null,
            CONSTRAINT fk_file_store_mandant FOREIGN KEY (mandant_id) REFERENCES mandants(id) ON DELETE CASCADE
        );

        CREATE INDEX ix_file_store_created_at ON file_store(created_at desc);
        CREATE INDEX ix_file_store_mandant ON file_store(mandant_id);
    END IF;
END $$;

