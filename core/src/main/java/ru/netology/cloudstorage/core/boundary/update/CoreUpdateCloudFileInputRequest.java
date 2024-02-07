package ru.netology.cloudstorage.core.boundary.update;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.netology.cloudstorage.contracts.core.boundary.update.UpdateCloudFileInputRequest;
import ru.netology.cloudstorage.contracts.core.model.CloudUser;
import ru.netology.cloudstorage.contracts.core.model.TraceId;

@RequiredArgsConstructor
@Getter
@Builder
public class CoreUpdateCloudFileInputRequest implements UpdateCloudFileInputRequest {
    private final TraceId traceId;

    private final CloudUser user;

    private final String fileName;

    private final String newFileName;
}
