package ru.netology.cloudstorage.core.boundary.delete;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import ru.netology.cloudstorage.contracts.core.boundary.delete.DeleteCloudFileStorageDeleteAction;
import ru.netology.cloudstorage.contracts.core.boundary.delete.DeleteCloudFileStorageDeleteActionRequest;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileException;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileExceptionCode;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileExceptionFactory;
import ru.netology.cloudstorage.contracts.core.model.StorageFile;
import ru.netology.cloudstorage.contracts.event.exception.CloudstorageEventPublisherException;
import ru.netology.cloudstorage.contracts.event.handler.CloudstorageEventPublisher;
import ru.netology.cloudstorage.contracts.event.model.delete.DeleteCloudFileError;
import ru.netology.cloudstorage.contracts.event.model.delete.StorageFileDeleted;
import ru.netology.cloudstorage.contracts.storage.repository.DeleteCloudFileStorageRepository;
import ru.netology.cloudstorage.core.event.delete.CoreDeleteCloudFileError;
import ru.netology.cloudstorage.core.event.delete.CoreStorageFileDeleted;

@RequiredArgsConstructor
@Builder
public class CoreDeleteCloudFileStorageDeleteAction implements DeleteCloudFileStorageDeleteAction {

    private final DeleteCloudFileStorageRepository storageRepository;

    private final CloudFileExceptionFactory exceptionFactory;

    private final CloudstorageEventPublisher eventPublisher;

    @Override
    public boolean delete(DeleteCloudFileStorageDeleteActionRequest request) {
        try {
            StorageFile storageFile = request.getStorageFile();

            storageRepository.delete(storageFile);

            StorageFileDeleted storageFileDeleted = CoreStorageFileDeleted.builder()
                    .cloudFile(request.getCloudFile())
                    .traceId(request.getTraceId())
                    .build();

            eventPublisher.publish(storageFileDeleted);

            return true;
        } catch (CloudstorageEventPublisherException publisherException) {
            throw exceptionFactory.create(CloudFileExceptionCode.STORAGE_FILE_DELETE_ERROR, request.getTraceId(),
                    publisherException);

        } catch (Exception ex) {
            DeleteCloudFileError cloudFileErrorEvent = CoreDeleteCloudFileError.builder()
                    .traceId(request.getTraceId())
                    .errorMessage(ex.getMessage())
                    .cloudFile(request.getCloudFile())
                    .build();

            eventPublisher.publish(cloudFileErrorEvent);

            if (ex instanceof CloudFileException) {
                throw ex;
            }

            throw exceptionFactory.create(CloudFileExceptionCode.STORAGE_FILE_DELETE_ERROR,
                    request.getTraceId(), ex);
        }
    }
}
