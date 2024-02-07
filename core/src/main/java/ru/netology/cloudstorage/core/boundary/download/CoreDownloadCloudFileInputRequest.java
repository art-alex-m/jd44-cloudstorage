package ru.netology.cloudstorage.core.boundary.download;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.netology.cloudstorage.contracts.core.boundary.download.DownloadCloudFileInputRequest;
import ru.netology.cloudstorage.contracts.core.model.CloudUser;
import ru.netology.cloudstorage.contracts.core.model.TraceId;

@RequiredArgsConstructor
@Getter
public class CoreDownloadCloudFileInputRequest implements DownloadCloudFileInputRequest {
    private final String fileName;

    private final TraceId traceId;

    private final CloudUser user;
}
