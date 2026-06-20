alter table mandant_settings
    add column if not exists course_report_template bigint;

create index if not exists ix_mandant_settings_course_report_template
    on mandant_settings(course_report_template);