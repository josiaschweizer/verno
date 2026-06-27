--we no longer require a tenant for mail log entries so we can make tenant less logs (eg for api calls from the get in touch formular on landing page)

ALTER TABLE mail_log
    ALTER COLUMN mandant_id DROP NOT NULL;