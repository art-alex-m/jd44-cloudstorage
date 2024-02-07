package ru.netology.cloudstorage.core.boundary.download;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import ru.netology.cloudstorage.contracts.core.boundary.download.DownloadCloudFileInput;
import ru.netology.cloudstorage.contracts.core.boundary.download.DownloadCloudFileInputRequest;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileExceptionCode;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileExceptionFactory;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.FileResource;
import ru.netology.cloudstorage.contracts.db.repository.DownloadCloudFileInputDbRepository;
import ru.netology.cloudstorage.contracts.storage.repository.DownloadCloudFileStorageRepository;

@RequiredArgsConstructor
@Builder
public class CoreDownloadCloudFileInteractor implements DownloadCloudFileInput {

    private final DownloadCloudFileInputDbRepository dbRepository;

    private final DownloadCloudFileStorageRepository storageRepository;

    private final CloudFileExceptionFactory exceptionFactory;

    @Override
    public FileResource getResource(DownloadCloudFileInputRequest request) {

        CloudFile cloudFile = dbRepository.findByUserAndFileNameAndReadyStatus(request.getUser(), request.getFileName())
                .orElseThrow(() -> exceptionFactory
                        .create(CloudFileExceptionCode.FILE_NOT_FOUND_ERROR, request.getTraceId()));

        return storageRepository.getResource(cloudFile.getStorageFile());
    }
}
