create table if not exists file_store (
    id varchar(36) primary key,
    filename varchar(512) not null,
    content_type varchar(128) not null,
    size bigint not null,
    checksum_sha256 varchar(128) not null,
    storage_key varchar(1024) not null,
    created_at timestamptz not null
);

create index if not exists ix_file_store_created_at on file_store(created_at desc);