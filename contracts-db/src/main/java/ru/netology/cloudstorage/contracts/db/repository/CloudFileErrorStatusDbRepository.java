package ru.netology.cloudstorage.contracts.db.repository;

import ru.netology.cloudstorage.contracts.core.model.CloudFile;

public interface CloudFileErrorStatusDbRepository {
    boolean save(CloudFile cloudFile);
}
