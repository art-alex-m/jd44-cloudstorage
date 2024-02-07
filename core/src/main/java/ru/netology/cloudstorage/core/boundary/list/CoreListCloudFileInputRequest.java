package ru.netology.cloudstorage.core.boundary.list;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.netology.cloudstorage.contracts.core.boundary.list.ListCloudFileInputRequest;
import ru.netology.cloudstorage.contracts.core.model.CloudUser;
import ru.netology.cloudstorage.contracts.core.model.TraceId;

@RequiredArgsConstructor
@Getter
public class CoreListCloudFileInputRequest implements ListCloudFileInputRequest {
    private final CloudUser user;
    private final int limit;
    private final TraceId traceId;
}
