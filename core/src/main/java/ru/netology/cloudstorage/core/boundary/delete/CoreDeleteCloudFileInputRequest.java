package ru.netology.cloudstorage.core.boundary.delete;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.netology.cloudstorage.contracts.core.boundary.delete.DeleteCloudFileInputRequest;
import ru.netology.cloudstorage.contracts.core.model.CloudUser;
import ru.netology.cloudstorage.contracts.core.model.TraceId;

@RequiredArgsConstructor
@Getter
public class CoreDeleteCloudFileInputRequest implements DeleteCloudFileInputRequest {
    private final String fileName;

    private final TraceId traceId;

    private final CloudUser user;
}
