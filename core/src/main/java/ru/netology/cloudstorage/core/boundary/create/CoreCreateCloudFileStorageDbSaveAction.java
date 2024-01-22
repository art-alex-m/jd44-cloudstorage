package ru.netology.cloudstorage.core.boundary.create;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileStorageDbSaveAction;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileStorageDbSaveActionRequest;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileException;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileExceptionCode;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileExceptionFactory;
import ru.netology.cloudstorage.contracts.db.repository.CreateCloudFileInputDbRepository;
import ru.netology.cloudstorage.contracts.event.exception.CloudstorageEventPublisherException;
import ru.netology.cloudstorage.contracts.event.handler.CloudstorageEventPublisher;
import ru.netology.cloudstorage.contracts.event.model.create.CreateCloudFileError;
import ru.netology.cloudstorage.contracts.event.model.create.StorageFileDbStored;
import ru.netology.cloudstorage.core.event.create.CoreCreateCloudFileError;
import ru.netology.cloudstorage.core.event.create.CoreStorageFileDbStored;

@RequiredArgsConstructor
@Builder
public class CoreCreateCloudFileStorageDbSaveAction implements CreateCloudFileStorageDbSaveAction {

    private final CreateCloudFileInputDbRepository dbRepository;

    private final CloudFileExceptionFactory exceptionFactory;

    private final CloudstorageEventPublisher eventPublisher;

    @Override
    public boolean save(CreateCloudFileStorageDbSaveActionRequest request) throws CloudFileException {

        try {
            dbRepository.save(request.getCloudFile());

            StorageFileDbStored storageFileDbStored = CoreStorageFileDbStored.builder()
                    .cloudFile(request.getCloudFile())
                    .traceId(request.getTraceId())
                    .build();

            eventPublisher.publish(storageFileDbStored);

        } catch (CloudstorageEventPublisherException publisherException) {
            throw exceptionFactory.create(CloudFileExceptionCode.DB_SAVE_STORAGE_FILE_ERROR, request.getTraceId(),
                    publisherException);

        } catch (Exception ex) {
            CreateCloudFileError createCloudFileError = CoreCreateCloudFileError.builder()
                    .errorMessage(ex.getMessage())
                    .traceId(request.getTraceId())
                    .cloudFile(request.getCloudFile())
                    .build();

            eventPublisher.publish(createCloudFileError);

            throw exceptionFactory.create(CloudFileExceptionCode.DB_SAVE_STORAGE_FILE_ERROR, request.getTraceId(), ex);
        }

        return true;
    }
}
