package ru.netology.cloudstorage.contracts.core.factory;

import ru.netology.cloudstorage.contracts.core.model.CloudFileStatus;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatusCode;

public interface CloudFileStatusFactory {
    CloudFileStatus create(CloudFileStatusCode code, String message);

    default CloudFileStatus create(CloudFileStatusCode code) {
        return create(code, null);
    }
}
