INSERT INTO cloud_files (id, user_id, file_name, created_at, updated_at)
VALUES ('7b7584a5-7391-359e-bca6-7e4e79b058e1', '09b35c6a-ffd1-4a17-8d53-2001b8a0e519', '1',
        '2024-02-05 12:10:10.872278', '2024-02-05 12:10:10.872280'),
       ('89349b51-fba7-34b6-86ee-836737a460e8', '09b35c6a-ffd1-4a17-8d53-2001b8a0e519', '2',
        '2024-02-05 12:16:45.284632', '2024-02-05 12:16:45.284633'),
       ('730f651e-14e8-34f6-90f4-3ef7b49030ca', '09b35c6a-ffd1-4a17-8d53-2001b8a0e519', '3',
        '2024-02-05 12:57:08.947730', '2024-02-05 12:57:08.947732'),
       ('f9307ceb-377f-34b8-9ffa-8fbc362d3142', '09b35c6a-ffd1-4a17-8d53-2001b8a0e519', '4',
        '2024-02-05 12:57:51.800748', '2024-02-05 12:57:51.800748'),
       ('572db379-122a-3bcb-bac9-8ebfe73e4b6f', '09b35c6a-ffd1-4a17-8d53-2001b8a0e519', '5',
        '2024-02-05 12:58:49.174390', '2024-02-05 12:58:49.174390');

INSERT INTO storage_files (id, cloud_file_id, size, file_name, media_type, created_at, updated_at)
VALUES ('cfa0bf2d-0b46-4ff5-86e3-5d1ec8f05772', '7b7584a5-7391-359e-bca6-7e4e79b058e1', 40477,
        'cfa0bf2d-0b46-4ff5-86e3-5d1ec8f05772', 'application/vnd.oasis.opendocument.text', '2024-02-05 12:10:10.979763',
        '2024-02-05 12:10:10.979769'),
       ('89de2ffd-bbf2-455d-82c3-faac38b47343', '89349b51-fba7-34b6-86ee-836737a460e8', 14507,
        '89de2ffd-bbf2-455d-82c3-faac38b47343', 'application/vnd.oasis.opendocument.spreadsheet',
        '2024-02-05 12:16:45.413714', '2024-02-05 12:16:45.413718'),
       ('22cbd90e-f7f4-4355-976b-2c6b2afea442', '730f651e-14e8-34f6-90f4-3ef7b49030ca', 186747,
        '22cbd90e-f7f4-4355-976b-2c6b2afea442', 'image/jpeg', '2024-02-05 12:57:09.087455',
        '2024-02-05 12:57:09.087457'),
       ('b9435ffd-0758-4036-894a-45c73a2943b9', 'f9307ceb-377f-34b8-9ffa-8fbc362d3142', 182186,
        'b9435ffd-0758-4036-894a-45c73a2943b9', 'image/jpeg', '2024-02-05 12:57:51.823713',
        '2024-02-05 12:57:51.823714'),
       ('ab6bbb0d-07b3-48bc-9edf-9b62a92e728b', '572db379-122a-3bcb-bac9-8ebfe73e4b6f', 34717,
        'ab6bbb0d-07b3-48bc-9edf-9b62a92e728b', 'image/png', '2024-02-05 12:58:49.199746',
        '2024-02-05 12:58:49.199748');

INSERT INTO cloud_file_statuses (id, cloud_file_id, code, trace_id_uuid, trace_id, created_at, updated_at)
VALUES ('866ad0ab-87b8-464a-af9c-9d607f1e5d3a', '7b7584a5-7391-359e-bca6-7e4e79b058e1', 'LOADING',
        'a1b3d4fb-a932-4d38-9524-61495ba023ea', 7719702333044710605, '2024-02-05 12:10:10.872255',
        '2024-02-05 12:10:10.872259'),
       ('3ac547e9-b0ce-4efd-8fe3-7d2a0d5f27e7', '7b7584a5-7391-359e-bca6-7e4e79b058e1', 'UPLOADED',
        'a1b3d4fb-a932-4d38-9524-61495ba023ea', 7719702333044710605, '2024-02-05 12:10:10.979847',
        '2024-02-05 12:10:10.979848'),
       ('54399bd3-b28d-4af0-be1f-0e33d7d48810', '7b7584a5-7391-359e-bca6-7e4e79b058e1', 'READY',
        'a1b3d4fb-a932-4d38-9524-61495ba023ea', 7719702333044710605, '2024-02-05 12:10:11.005098',
        '2024-02-05 12:10:11.005099'),
       ('91dc7fe8-d4a4-4bb0-b91f-59084e5fbbb7', '89349b51-fba7-34b6-86ee-836737a460e8', 'LOADING',
        '8ca6b733-2c41-474e-92cd-e03d4b72d81f', 5026095445059316545, '2024-02-05 12:16:45.284613',
        '2024-02-05 12:16:45.284618'),
       ('f725659f-5180-4f7c-a7c1-386e911d76cd', '89349b51-fba7-34b6-86ee-836737a460e8', 'UPLOADED',
        '8ca6b733-2c41-474e-92cd-e03d4b72d81f', 5026095445059316545, '2024-02-05 12:16:45.413788',
        '2024-02-05 12:16:45.413789'),
       ('c70ce6cc-b1dd-4d27-a8e7-09464d42cdfd', '89349b51-fba7-34b6-86ee-836737a460e8', 'READY',
        '8ca6b733-2c41-474e-92cd-e03d4b72d81f', 5026095445059316545, '2024-02-05 12:16:45.483185',
        '2024-02-05 12:16:45.483186'),
       ('8ea5e8cd-9566-4d01-bb70-ab1ac869889c', '730f651e-14e8-34f6-90f4-3ef7b49030ca', 'LOADING',
        '8f624cbd-dbfd-400f-9da3-4725a6404588', 4734390618587751115, '2024-02-05 12:57:08.947717',
        '2024-02-05 12:57:08.947719'),
       ('0b05898b-f5c6-49ee-b50b-e721151df2fa', '730f651e-14e8-34f6-90f4-3ef7b49030ca', 'UPLOADED',
        '8f624cbd-dbfd-400f-9da3-4725a6404588', 4734390618587751115, '2024-02-05 12:57:09.087484',
        '2024-02-05 12:57:09.087484'),
       ('ecbe668a-f07c-4aa0-bef1-fed525d63341', '730f651e-14e8-34f6-90f4-3ef7b49030ca', 'READY',
        '8f624cbd-dbfd-400f-9da3-4725a6404588', 4734390618587751115, '2024-02-05 12:57:09.108831',
        '2024-02-05 12:57:09.108832'),
       ('8360f313-225f-445d-aff4-24778e78c4bc', 'f9307ceb-377f-34b8-9ffa-8fbc362d3142', 'LOADING',
        '1f825497-18e7-4d0b-b566-7c3444accf2f', 4237488619571303780, '2024-02-05 12:57:51.800745',
        '2024-02-05 12:57:51.800746'),
       ('0d5ba283-78fb-482d-8b78-10cd7497aca3', 'f9307ceb-377f-34b8-9ffa-8fbc362d3142', 'UPLOADED',
        '1f825497-18e7-4d0b-b566-7c3444accf2f', 4237488619571303780, '2024-02-05 12:57:51.823751',
        '2024-02-05 12:57:51.823751'),
       ('a509096b-3581-4da6-a30c-ff32424bc360', 'f9307ceb-377f-34b8-9ffa-8fbc362d3142', 'ERROR',
        '1f825497-18e7-4d0b-b566-7c3444accf2f', 4237488619571303780, '2024-02-05 12:57:51.837762',
        '2024-02-05 12:57:51.837762'),
       ('63333a2e-045a-497c-9a23-2913551ef704', '572db379-122a-3bcb-bac9-8ebfe73e4b6f', 'LOADING',
        'cfdda30e-b0d7-4721-8353-0fe2c9470008', 1041498095029110565, '2024-02-05 12:58:49.174385',
        '2024-02-05 12:58:49.174386'),
       ('e5d18963-0d25-4ce3-9f43-c73c0fdeb7dd', '572db379-122a-3bcb-bac9-8ebfe73e4b6f', 'UPLOADED',
        'cfdda30e-b0d7-4721-8353-0fe2c9470008', 1041498095029110565, '2024-02-05 12:58:49.199803',
        '2024-02-05 12:58:49.199805'),
       ('b1ea99c2-2aa7-4483-b21a-fef3487a16e7', '572db379-122a-3bcb-bac9-8ebfe73e4b6f', 'READY',
        'cfdda30e-b0d7-4721-8353-0fe2c9470008', 1041498095029110565, '2024-02-05 12:58:49.217085',
        '2024-02-05 12:58:49.217085');

INSERT INTO cloud_file_status_latest (cloud_file_id, cloud_file_status_id)
VALUES ('7b7584a5-7391-359e-bca6-7e4e79b058e1', '54399bd3-b28d-4af0-be1f-0e33d7d48810'),
       ('89349b51-fba7-34b6-86ee-836737a460e8', 'c70ce6cc-b1dd-4d27-a8e7-09464d42cdfd'),
       ('730f651e-14e8-34f6-90f4-3ef7b49030ca', 'ecbe668a-f07c-4aa0-bef1-fed525d63341'),
       ('f9307ceb-377f-34b8-9ffa-8fbc362d3142', 'a509096b-3581-4da6-a30c-ff32424bc360'),
       ('572db379-122a-3bcb-bac9-8ebfe73e4b6f', 'b1ea99c2-2aa7-4483-b21a-fef3487a16e7');
