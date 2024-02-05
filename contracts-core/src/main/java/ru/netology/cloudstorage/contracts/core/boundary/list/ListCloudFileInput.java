package ru.netology.cloudstorage.contracts.core.boundary.list;

public interface ListCloudFileInput {
    ListCloudFileInputResponse find(ListCloudFileInputRequest request);
}
