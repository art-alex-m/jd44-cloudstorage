package ru.netology.cloudstorage.core.boundary.create;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileInput;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileInputRequest;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileInputResponse;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileException;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileExceptionCode;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileExceptionFactory;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileStatusFactory;
import ru.netology.cloudstorage.contracts.core.factory.CreateCloudFileInputResponseFactory;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatusCode;
import ru.netology.cloudstorage.contracts.db.repository.CreateCloudFileInputDbRepository;
import ru.netology.cloudstorage.contracts.event.handler.CloudstorageEventPublisher;
import ru.netology.cloudstorage.contracts.event.model.create.CloudFileDbCreated;
import ru.netology.cloudstorage.core.event.create.CoreCloudFileDbCreated;
import ru.netology.cloudstorage.core.model.CoreCloudFile;

import java.util.UUID;

@RequiredArgsConstructor
@Builder
public class CoreCreateCloudFileInteractor implements CreateCloudFileInput {

    private final CloudFileStatusFactory statusFactory;

    private final CreateCloudFileInputResponseFactory responseFactory;

    private final CreateCloudFileInputDbRepository dbRepository;

    private final CloudstorageEventPublisher eventPublisher;

    private final CloudFileExceptionFactory exceptionFactory;

    @Override
    public CreateCloudFileInputResponse create(CreateCloudFileInputRequest request) throws CloudFileException {

        try {
            CloudFile cloudFile = CoreCloudFile.builder()
                    .id(UUID.randomUUID())
                    .fileName(request.getUserFile().getFileName())
                    .status(statusFactory.create(CloudFileStatusCode.LOADING, request.getTraceId()))
                    .user(request.getUser())
                    .build();

            dbRepository.save(cloudFile);

            CloudFileDbCreated event = CoreCloudFileDbCreated.builder()
                    .cloudFile(cloudFile)
                    .userFile(request.getUserFile())
                    .traceId(request.getTraceId())
                    .build();

            eventPublisher.publish(event);

            return responseFactory.create(cloudFile);
        } catch (Exception ex) {
            throw exceptionFactory.create(CloudFileExceptionCode.DB_CREATE_ERROR, request.getTraceId(), ex);
        }
    }
}
