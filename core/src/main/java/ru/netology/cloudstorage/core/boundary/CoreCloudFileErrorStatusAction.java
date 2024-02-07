package ru.netology.cloudstorage.core.boundary;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import ru.netology.cloudstorage.contracts.core.boundary.CloudFileErrorStatusAction;
import ru.netology.cloudstorage.contracts.core.boundary.CloudFileErrorStatusActionRequest;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileException;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileExceptionCode;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileExceptionFactory;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileStatusFactory;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatusCode;
import ru.netology.cloudstorage.contracts.db.repository.CloudFileErrorStatusDbRepository;
import ru.netology.cloudstorage.core.model.CoreCloudFile;

@RequiredArgsConstructor
@Builder
public class CoreCloudFileErrorStatusAction implements CloudFileErrorStatusAction {

    private final CloudFileErrorStatusDbRepository dbRepository;

    private final CloudFileStatusFactory statusFactory;

    private final CloudFileExceptionFactory exceptionFactory;

    @Override
    public boolean update(CloudFileErrorStatusActionRequest request) throws CloudFileException {

        try {

            CloudFile cloudFile = CoreCloudFile.from(request.getCloudFile())
                    .updatedAt(request.getCloudFile().getUpdatedAt())
                    .status(statusFactory.create(CloudFileStatusCode.ERROR, request.getTraceId(),
                            request.getErrorMessage()))
                    .build();

            return dbRepository.save(cloudFile);
        } catch (Exception ex) {
            throw exceptionFactory.create(CloudFileExceptionCode.DB_SAVE_STATUS_ERROR, request.getTraceId(), ex);
        }
    }
}
