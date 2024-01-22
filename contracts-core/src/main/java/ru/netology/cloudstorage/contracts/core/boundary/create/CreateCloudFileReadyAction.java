package ru.netology.cloudstorage.contracts.core.boundary.create;

import ru.netology.cloudstorage.contracts.core.exception.CloudFileException;

public interface CreateCloudFileReadyAction {
    boolean update(CreateCloudFileReadyActionRequest request) throws CloudFileException;
}
