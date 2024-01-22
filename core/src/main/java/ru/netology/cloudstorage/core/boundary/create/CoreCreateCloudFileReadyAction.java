package ru.netology.cloudstorage.core.boundary.create;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileReadyAction;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileReadyActionRequest;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileException;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileExceptionCode;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileExceptionFactory;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileStatusFactory;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatusCode;
import ru.netology.cloudstorage.contracts.db.repository.CreateCloudFileInputDbRepository;
import ru.netology.cloudstorage.contracts.event.exception.CloudstorageEventPublisherException;
import ru.netology.cloudstorage.contracts.event.handler.CloudstorageEventPublisher;
import ru.netology.cloudstorage.contracts.event.model.create.CloudFileIsReady;
import ru.netology.cloudstorage.contracts.event.model.create.CreateCloudFileError;
import ru.netology.cloudstorage.core.event.create.CoreCloudFileIsReady;
import ru.netology.cloudstorage.core.event.create.CoreCreateCloudFileError;
import ru.netology.cloudstorage.core.model.CoreCloudFile;

@RequiredArgsConstructor
@Builder
public class CoreCreateCloudFileReadyAction implements CreateCloudFileReadyAction {

    private final CloudFileStatusFactory statusFactory;

    private final CreateCloudFileInputDbRepository dbRepository;

    private final CloudFileExceptionFactory exceptionFactory;

    private final CloudstorageEventPublisher eventPublisher;

    @Override
    public boolean update(CreateCloudFileReadyActionRequest request) throws CloudFileException {

        try {
            CloudFile cloudFile = ((CoreCloudFile) request.getCloudFile()).toBuilder()
                    .status(statusFactory.create(CloudFileStatusCode.READY, request.getTraceId()))
                    .build();

            dbRepository.save(cloudFile);

            CloudFileIsReady cloudFileIsReady = CoreCloudFileIsReady.builder()
                    .traceId(request.getTraceId())
                    .cloudFile(cloudFile)
                    .build();

            eventPublisher.publish(cloudFileIsReady);

        } catch (CloudstorageEventPublisherException publisherException) {
            throw exceptionFactory.create(CloudFileExceptionCode.DB_SAVE_IS_READY_ERROR, request.getTraceId(),
                    publisherException);

        } catch (Exception ex) {
            CreateCloudFileError createCloudFileError = CoreCreateCloudFileError.builder()
                    .errorMessage(ex.getMessage())
                    .cloudFile(request.getCloudFile())
                    .build();

            eventPublisher.publish(createCloudFileError);

            throw exceptionFactory.create(CloudFileExceptionCode.DB_SAVE_IS_READY_ERROR, request.getTraceId(), ex);
        }

        return true;
    }
}
