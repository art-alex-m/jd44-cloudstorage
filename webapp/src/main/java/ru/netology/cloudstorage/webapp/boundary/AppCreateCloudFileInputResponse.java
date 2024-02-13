package ru.netology.cloudstorage.webapp.boundary;

import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileInputResponse;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatus;

import java.util.UUID;

public class AppCreateCloudFileInputResponse extends AppCloudFileInputResponse implements CreateCloudFileInputResponse {
    public AppCreateCloudFileInputResponse(UUID cloudFileId, CloudFileStatus cloudFileStatus) {
        super(cloudFileId, cloudFileStatus);
    }
}
