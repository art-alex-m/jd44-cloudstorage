package ru.netology.cloudstorage.core.boundary.delete;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import ru.netology.cloudstorage.contracts.core.boundary.delete.DeleteCloudFileInput;
import ru.netology.cloudstorage.contracts.core.boundary.delete.DeleteCloudFileInputRequest;
import ru.netology.cloudstorage.contracts.core.boundary.delete.DeleteCloudFileInputResponse;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileException;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileExceptionCode;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileExceptionFactory;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileStatusFactory;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatusCode;
import ru.netology.cloudstorage.contracts.db.repository.DeleteCloudFileInputDbRepository;
import ru.netology.cloudstorage.contracts.event.exception.CloudstorageEventPublisherException;
import ru.netology.cloudstorage.contracts.event.handler.CloudstorageEventPublisher;
import ru.netology.cloudstorage.contracts.event.model.delete.DeleteCloudFileError;
import ru.netology.cloudstorage.contracts.event.model.delete.StorageFileDbDeleted;
import ru.netology.cloudstorage.core.event.delete.CoreDeleteCloudFileError;
import ru.netology.cloudstorage.core.event.delete.CoreStorageFileDbDeleted;
import ru.netology.cloudstorage.core.model.CoreCloudFile;

@RequiredArgsConstructor
@Builder
public class CoreDeleteCloudFileInteractor implements DeleteCloudFileInput {

    private final DeleteCloudFileInputDbRepository dbRepository;

    private final CloudFileStatusFactory statusFactory;

    private final CloudFileExceptionFactory exceptionFactory;

    private final CloudstorageEventPublisher eventPublisher;

    @Override
    public DeleteCloudFileInputResponse delete(DeleteCloudFileInputRequest request) throws CloudFileException {
        CloudFile cloudFile = null;

        try {
            cloudFile = dbRepository.findByUserAndFileNameAndReadyStatus(request.getUser(), request.getFileName())
                    .orElseThrow(() -> exceptionFactory.create(CloudFileExceptionCode.FILE_NOT_FOUND_ERROR,
                            request.getTraceId()));

            dbRepository.delete(cloudFile.getStorageFile());

            CloudFile updatedCloudFile = CoreCloudFile.from(cloudFile)
                    .storageFile(null)
                    .status(statusFactory.create(CloudFileStatusCode.DELETED, request.getTraceId()))
                    .build();

            dbRepository.update(updatedCloudFile);

            StorageFileDbDeleted storageFileDbDeleted = CoreStorageFileDbDeleted.builder()
                    .cloudFile(updatedCloudFile)
                    .storageFile(cloudFile.getStorageFile())
                    .traceId(request.getTraceId())
                    .build();

            eventPublisher.publish(storageFileDbDeleted);

            return new CoreDeleteCloudFileInputResponse(updatedCloudFile.getId(), updatedCloudFile.getStatus());
        } catch (CloudstorageEventPublisherException publisherException) {
            throw exceptionFactory.create(CloudFileExceptionCode.DB_STORAGE_FILE_DELETE_ERROR, request.getTraceId(),
                    publisherException);

        } catch (Exception ex) {
            DeleteCloudFileError cloudFileErrorEvent = CoreDeleteCloudFileError.builder()
                    .traceId(request.getTraceId())
                    .errorMessage(ex.getMessage())
                    .cloudFile(cloudFile)
                    .build();

            eventPublisher.publish(cloudFileErrorEvent);

            if (ex instanceof CloudFileException) {
                throw ex;
            }

            throw exceptionFactory.create(CloudFileExceptionCode.DB_STORAGE_FILE_DELETE_ERROR,
                    request.getTraceId(), ex);
        }
    }
}
