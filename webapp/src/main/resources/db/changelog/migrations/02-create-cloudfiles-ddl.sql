-- liquibase formatted sql
-- changeset artalexm:create-cloud-files
create table cloud_files
(
    id         uuid          not null primary key,
    user_id    uuid          not null,
    file_name  varchar(1000) not null,
    created_at timestamp     not null,
    updated_at timestamp     not null
);

-- changeset artalexm:create-cloud-files-indexes
create unique index ix_cloud_files_user_id_file_name on cloud_files (user_id, file_name);

-- changeset artalexm:create-cloud-file-statuses
create table cloud_file_statuses
(
    id            uuid        not null primary key,
    cloud_file_id uuid        not null,
    code          varchar(50) not null,
    trace_id_uuid uuid        not null,
    trace_id      bigint      not null,
    created_at    timestamp   not null,
    updated_at    timestamp   not null,
    constraint fk_cloud_file_statuses foreign key (cloud_file_id) references cloud_files (id)
        on delete cascade on update cascade
);

-- changeset artalexm:create-cloud-file-status-messages
create table cloud_file_status_messages
(
    id      uuid          not null primary key,
    message varchar(1000) not null,
    constraint fk_cloud_file_status_messages foreign key (id) references cloud_file_statuses (id)
        on delete cascade on update cascade
);

-- changeset artalexm:create-cloud-file-status-latest
create table cloud_file_status_latest
(
    cloud_file_id        uuid not null primary key,
    cloud_file_status_id uuid not null,
    constraint fk_cloud_file_status_latest_cloud_file foreign key (cloud_file_id) references cloud_files (id)
        on delete cascade on update cascade,
    constraint fk_cloud_file_status_latest_status foreign key (cloud_file_status_id) references cloud_file_statuses (id)
        on delete restrict on update cascade
);

-- changeset artalexm:create-storage-files
create table storage_files
(
    id            uuid          not null primary key,
    cloud_file_id uuid          not null,
    size bigint not null,
    file_name     varchar(1000) not null,
    media_type    varchar(100)  not null,
    created_at    timestamp     not null,
    updated_at    timestamp     not null,
    constraint fk_storage_file_to_cloud_file foreign key (cloud_file_id) references cloud_files (id)
        on delete restrict on update cascade
);
