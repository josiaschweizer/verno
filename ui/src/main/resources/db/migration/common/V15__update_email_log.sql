ALTER TABLE mail_log RENAME COLUMN tenant_id TO mandant_id;

ALTER TABLE mail_log
    ADD CONSTRAINT fk_mail_log_mandant
    FOREIGN KEY (mandant_id) REFERENCES mandants(id) ON DELETE CASCADE;

CREATE INDEX ix_mail_log_mandant ON mail_log(mandant_id);

