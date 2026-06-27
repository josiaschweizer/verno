-- Drop the existing file_store table and recreate it with correct schema
drop table if exists file_store cascade;

create table file_store (
    id bigserial primary key,
    filename varchar(512) not null,
    content_type varchar(128) not null,
    size bigint not null,
    checksum_sha256 varchar(128) not null,
    storage_key varchar(1024),
    created_at timestamptz not null,
    mandant_id bigint not null,
    constraint fk_file_store_mandant foreign key (mandant_id) references mandants(id) on delete cascade
);

create index if not exists ix_file_store_created_at on file_store(created_at desc);
create index if not exists ix_file_store_mandant on file_store(mandant_id);

