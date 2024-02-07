package ru.netology.cloudstorage.core.boundary.create;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileStorageUploadAction;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileStorageUploadActionRequest;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileException;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileExceptionCode;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileExceptionFactory;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileStatusFactory;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatusCode;
import ru.netology.cloudstorage.contracts.core.model.StorageFile;
import ru.netology.cloudstorage.contracts.event.exception.CloudstorageEventPublisherException;
import ru.netology.cloudstorage.contracts.event.handler.CloudstorageEventPublisher;
import ru.netology.cloudstorage.contracts.event.model.create.CreateCloudFileError;
import ru.netology.cloudstorage.contracts.event.model.create.StorageFileUploaded;
import ru.netology.cloudstorage.contracts.storage.repository.CreateCloudFileStorageUploadRepository;
import ru.netology.cloudstorage.core.event.create.CoreCreateCloudFileError;
import ru.netology.cloudstorage.core.event.create.CoreStorageFileUploaded;
import ru.netology.cloudstorage.core.model.CoreCloudFile;

@RequiredArgsConstructor
@Builder
public class CoreCreateCloudFileStorageUploadAction implements CreateCloudFileStorageUploadAction {

    private final CreateCloudFileStorageUploadRepository uploadRepository;

    private final CloudFileStatusFactory statusFactory;

    private final CloudFileExceptionFactory exceptionFactory;

    private final CloudstorageEventPublisher eventPublisher;

    @Override
    public boolean upload(CreateCloudFileStorageUploadActionRequest request) throws CloudFileException {

        try {
            StorageFile storageFile = uploadRepository.save(request.getUserFile());

            CloudFile cloudFile = ((CoreCloudFile) request.getCloudFile()).toBuilder()
                    .storageFile(storageFile)
                    .status(statusFactory.create(CloudFileStatusCode.UPLOADED, request.getTraceId()))
                    .build();

            StorageFileUploaded storageFileUploaded = CoreStorageFileUploaded.builder()
                    .traceId(request.getTraceId())
                    .cloudFile(cloudFile)
                    .build();

            eventPublisher.publish(storageFileUploaded);

        } catch (CloudstorageEventPublisherException publisherException) {
            throw exceptionFactory.create(CloudFileExceptionCode.UPLOAD_ERROR, request.getTraceId(),
                    publisherException);

        } catch (Exception ex) {
            CreateCloudFileError createCloudFileError = CoreCreateCloudFileError.builder()
                    .errorMessage(ex.getMessage())
                    .traceId(request.getTraceId())
                    .cloudFile(request.getCloudFile())
                    .build();

            eventPublisher.publish(createCloudFileError);

            throw exceptionFactory.create(CloudFileExceptionCode.UPLOAD_ERROR, request.getTraceId(), ex);
        }

        return true;
    }
}
