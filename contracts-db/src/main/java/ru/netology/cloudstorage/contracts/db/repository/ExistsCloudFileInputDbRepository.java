package ru.netology.cloudstorage.contracts.db.repository;

import java.util.UUID;

public interface ExistsCloudFileInputDbRepository {
    boolean exists(UUID cloudFileId);
}
