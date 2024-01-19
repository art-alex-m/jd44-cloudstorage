package ru.netology.cloudstorage.contracts.core.input;

import ru.netology.cloudstorage.contracts.core.model.CloudFileStatus;
import ru.netology.cloudstorage.contracts.core.model.Traceable;

import java.util.UUID;

public interface CreateCloudFileInputResponse extends Traceable {
    UUID getCloudFileId();

    CloudFileStatus getCloudFileStatus();
}
