package ru.netology.cloudstorage.core.boundary.update;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import ru.netology.cloudstorage.contracts.core.boundary.update.UpdateCloudFileInput;
import ru.netology.cloudstorage.contracts.core.boundary.update.UpdateCloudFileInputRequest;
import ru.netology.cloudstorage.contracts.core.boundary.update.UpdateCloudFileInputResponse;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileException;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileExceptionCode;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileExceptionFactory;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.db.repository.UpdateCloudFileInputDbRepository;
import ru.netology.cloudstorage.contracts.event.exception.CloudstorageEventPublisherException;
import ru.netology.cloudstorage.contracts.event.handler.CloudstorageEventPublisher;
import ru.netology.cloudstorage.contracts.event.model.update.CloudFileUpdated;
import ru.netology.cloudstorage.contracts.event.model.update.UpdateCloudFileError;
import ru.netology.cloudstorage.core.event.update.CoreCloudFileUpdated;
import ru.netology.cloudstorage.core.event.update.CoreUpdateCloudFileError;
import ru.netology.cloudstorage.core.model.CoreCloudFile;

@RequiredArgsConstructor
@Builder
public class CoreUpdateCloudFileInteractor implements UpdateCloudFileInput {

    private final UpdateCloudFileInputDbRepository dbRepository;

    private final CloudstorageEventPublisher eventPublisher;

    private final CloudFileExceptionFactory exceptionFactory;

    @Override
    public UpdateCloudFileInputResponse update(UpdateCloudFileInputRequest request) {
        CloudFile cloudFile = null;

        try {
            if (!dbRepository.uniqueNewName(request.getUser(), request.getNewFileName())) {
                throw exceptionFactory.create(CloudFileExceptionCode.NO_UNIQUE_FILE_NAME_ERROR, request.getTraceId());
            }

            cloudFile = dbRepository.findByUserAndFileName(request.getUser(), request.getFileName())
                    .orElseThrow(() -> exceptionFactory
                            .create(CloudFileExceptionCode.FILE_NOT_FOUND_ERROR, request.getTraceId()));

            CloudFile updatedCloudFile = CoreCloudFile.from(cloudFile)
                    .fileName(request.getNewFileName())
                    .build();

            dbRepository.update(updatedCloudFile);

            CloudFileUpdated cloudFileUpdated = CoreCloudFileUpdated.builder()
                    .cloudFile(updatedCloudFile)
                    .traceId(request.getTraceId())
                    .build();

            eventPublisher.publish(cloudFileUpdated);

            return new CoreUpdateCloudFileInputResponse(updatedCloudFile.getId(), updatedCloudFile.getStatus());

        } catch (CloudstorageEventPublisherException publisherException) {
            throw exceptionFactory.create(CloudFileExceptionCode.DB_UPDATE_ERROR, request.getTraceId(),
                    publisherException);

        } catch (Exception ex) {
            UpdateCloudFileError updateCloudFileError = CoreUpdateCloudFileError.builder()
                    .errorMessage(ex.getMessage())
                    .traceId(request.getTraceId())
                    .cloudFile(cloudFile)
                    .build();

            eventPublisher.publish(updateCloudFileError);

            if (ex instanceof CloudFileException) {
                throw ex;
            }

            throw exceptionFactory.create(CloudFileExceptionCode.DB_UPDATE_ERROR, request.getTraceId(), ex);
        }
    }
}
