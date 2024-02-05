package ru.netology.cloudstorage.core.boundary.update;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.netology.cloudstorage.contracts.core.boundary.update.UpdateCloudFileInputResponse;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatus;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class CoreUpdateCloudFileInputResponse implements UpdateCloudFileInputResponse {
    private final UUID cloudFileId;

    private final CloudFileStatus cloudFileStatus;
}
