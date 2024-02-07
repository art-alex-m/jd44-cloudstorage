package ru.netology.cloudstorage.contracts.core.boundary.list;

import ru.netology.cloudstorage.contracts.core.model.CloudFile;

import java.util.List;

public interface ListCloudFileInputResponse {
    List<CloudFile> getCloudFiles();
}
