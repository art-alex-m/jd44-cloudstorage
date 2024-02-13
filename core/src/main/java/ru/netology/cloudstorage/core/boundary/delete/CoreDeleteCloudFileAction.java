package ru.netology.cloudstorage.core.boundary.delete;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import ru.netology.cloudstorage.contracts.core.boundary.delete.DeleteCloudFileAction;
import ru.netology.cloudstorage.contracts.core.boundary.delete.DeleteCloudFileActionRequest;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileException;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileExceptionCode;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileExceptionFactory;
import ru.netology.cloudstorage.contracts.db.repository.DeleteCloudFileActionDbRepository;
import ru.netology.cloudstorage.contracts.event.exception.CloudstorageEventPublisherException;
import ru.netology.cloudstorage.contracts.event.handler.CloudstorageEventPublisher;
import ru.netology.cloudstorage.contracts.event.model.delete.CloudFileDeleted;
import ru.netology.cloudstorage.contracts.event.model.delete.DeleteCloudFileError;
import ru.netology.cloudstorage.core.event.delete.CoreCloudFileDeleted;
import ru.netology.cloudstorage.core.event.delete.CoreDeleteCloudFileError;

@RequiredArgsConstructor
@Builder
public class CoreDeleteCloudFileAction implements DeleteCloudFileAction {

    private final DeleteCloudFileActionDbRepository dbRepository;

    private final CloudFileExceptionFactory exceptionFactory;

    private final CloudstorageEventPublisher eventPublisher;

    @Override
    public boolean delete(DeleteCloudFileActionRequest request) throws CloudFileException {

        try {
            dbRepository.delete(request.getCloudFile());

            CloudFileDeleted fileDeleted = CoreCloudFileDeleted.builder()
                    .cloudFile(request.getCloudFile())
                    .traceId(request.getTraceId())
                    .build();

            eventPublisher.publish(fileDeleted);

            return true;
        } catch (CloudstorageEventPublisherException publisherException) {
            throw exceptionFactory.create(CloudFileExceptionCode.CLOUD_FILE_DELETE_ERROR, request.getTraceId(),
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

            throw exceptionFactory.create(CloudFileExceptionCode.CLOUD_FILE_DELETE_ERROR,
                    request.getTraceId(), ex);
        }
    }
}
