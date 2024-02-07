package ru.netology.cloudstorage.contracts.core.boundary;

import ru.netology.cloudstorage.contracts.core.exception.CloudFileException;

public interface CloudFileErrorStatusAction {
    boolean update(CloudFileErrorStatusActionRequest request) throws CloudFileException;
}
