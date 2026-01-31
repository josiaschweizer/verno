insert into roles(name)
values ('ADMIN'), ('USER')
    on conflict do nothing;

update app_meta
set value = '2',
    updated_at = now()
where key = 'schema_version';