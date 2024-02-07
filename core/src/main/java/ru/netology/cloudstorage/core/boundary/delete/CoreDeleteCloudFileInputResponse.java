package ru.netology.cloudstorage.core.boundary.delete;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.netology.cloudstorage.contracts.core.boundary.delete.DeleteCloudFileInputResponse;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatus;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class CoreDeleteCloudFileInputResponse implements DeleteCloudFileInputResponse {
    private final UUID cloudFileId;

    private final CloudFileStatus cloudFileStatus;
}
