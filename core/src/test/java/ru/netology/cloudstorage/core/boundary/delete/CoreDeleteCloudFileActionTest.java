package ru.netology.cloudstorage.core.boundary.delete;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.netology.cloudstorage.contracts.core.boundary.delete.DeleteCloudFileActionRequest;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileException;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileExceptionCode;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileExceptionFactory;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.db.repository.DeleteCloudFileActionDbRepository;
import ru.netology.cloudstorage.contracts.event.exception.CloudstorageEventPublisherException;
import ru.netology.cloudstorage.contracts.event.handler.CloudstorageEventPublisher;
import ru.netology.cloudstorage.contracts.event.model.delete.CloudFileDeleted;
import ru.netology.cloudstorage.contracts.event.model.delete.DeleteCloudFileError;
import ru.netology.cloudstorage.core.factory.CloudFileTestDataFactory;
import ru.netology.cloudstorage.core.factory.CoreCloudFileExceptionFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class CoreDeleteCloudFileActionTest {
    @Mock
    DeleteCloudFileActionDbRepository dbRepository;

    @Spy
    CloudFileExceptionFactory exceptionFactory = new CoreCloudFileExceptionFactory();

    @Mock
    CloudstorageEventPublisher eventPublisher;

    @Mock
    CloudFile cloudFile;

    @Mock
    DeleteCloudFileActionRequest request;

    @InjectMocks
    CoreDeleteCloudFileAction sut;

    @BeforeEach
    void setUp() {
        given(request.getTraceId()).willReturn(CloudFileTestDataFactory.getTraceId());
    }

    @Test
    void givenRequest_whenDelete_thenSuccess() {
        given(request.getCloudFile()).willReturn(cloudFile);
        ArgumentCaptor<CloudFileDeleted> fileDeletedCaptor = ArgumentCaptor.forClass(CloudFileDeleted.class);

        boolean result = sut.delete(request);

        assertTrue(result);
        verifyNoInteractions(exceptionFactory);
        verify(dbRepository, times(1)).delete(cloudFile);
        verifyNoMoreInteractions(dbRepository);
        verify(eventPublisher, times(1)).publish(fileDeletedCaptor.capture());
        verifyNoMoreInteractions(eventPublisher);
        CloudFileDeleted fileDeleted = fileDeletedCaptor.getValue();
        assertNotNull(fileDeleted);
        assertEquals(cloudFile, fileDeleted.getCloudFile());
        assertEquals(request.getTraceId(), fileDeleted.getTraceId());
        assertNotNull(fileDeleted.getCreatedAt());
    }

    @Test
    void givenPublisherException_whenDelete_thenCloudException() {
        given(request.getCloudFile()).willReturn(cloudFile);
        ArgumentCaptor<CloudFileDeleted> fileDeletedCaptor = ArgumentCaptor.forClass(CloudFileDeleted.class);
        CloudstorageEventPublisherException publisherException = new CloudstorageEventPublisherException(
                "publisherException");
        given(eventPublisher.publish(any())).willThrow(publisherException);

        Executable result = () -> sut.delete(request);

        assertThrows(CloudFileException.class, result);
        verify(dbRepository, times(1)).delete(cloudFile);
        verifyNoMoreInteractions(dbRepository);
        verify(eventPublisher, times(1)).publish(fileDeletedCaptor.capture());
        verifyNoMoreInteractions(eventPublisher);
        CloudFileDeleted fileDeleted = fileDeletedCaptor.getValue();
        assertNotNull(fileDeleted);
        assertEquals(cloudFile, fileDeleted.getCloudFile());
        assertEquals(request.getTraceId(), fileDeleted.getTraceId());
        assertNotNull(fileDeleted.getCreatedAt());
        verify(exceptionFactory, times(1))
                .create(CloudFileExceptionCode.CLOUD_FILE_DELETE_ERROR, request.getTraceId(), publisherException);
        verifyNoMoreInteractions(exceptionFactory);
    }

    @Test
    void givenRuntimeException_whenDelete_thenCloudException() {
        given(request.getCloudFile()).willReturn(cloudFile);
        RuntimeException runtimeException = new RuntimeException("runtimeException");
        given(dbRepository.delete(cloudFile)).willThrow(runtimeException);
        ArgumentCaptor<DeleteCloudFileError> fileErrorCaptor = ArgumentCaptor.forClass(DeleteCloudFileError.class);

        Executable result = () -> sut.delete(request);

        assertThrows(CloudFileException.class, result);
        verify(dbRepository, times(1)).delete(cloudFile);
        verifyNoMoreInteractions(dbRepository);
        verify(eventPublisher, times(1)).publish(fileErrorCaptor.capture());
        verifyNoMoreInteractions(eventPublisher);
        DeleteCloudFileError fileDeleteError = fileErrorCaptor.getValue();
        assertNotNull(fileDeleteError);
        assertEquals(cloudFile, fileDeleteError.getCloudFile());
        assertEquals(request.getTraceId(), fileDeleteError.getTraceId());
        assertNotNull(fileDeleteError.getCreatedAt());
        assertEquals(runtimeException.getMessage(), fileDeleteError.getErrorMessage());
        verify(exceptionFactory, times(1))
                .create(CloudFileExceptionCode.CLOUD_FILE_DELETE_ERROR, request.getTraceId(), runtimeException);
        verifyNoMoreInteractions(exceptionFactory);
    }

    @Test
    void givenCloudException_whenDelete_thenCloudException() {
        given(request.getCloudFile()).willReturn(cloudFile);
        CloudFileException cloudException = new CloudFileException(CloudFileExceptionCode.CLOUD_FILE_DELETE_ERROR,
                request.getTraceId());
        given(dbRepository.delete(cloudFile)).willThrow(cloudException);
        ArgumentCaptor<DeleteCloudFileError> fileErrorCaptor = ArgumentCaptor.forClass(DeleteCloudFileError.class);

        Executable result = () -> sut.delete(request);

        assertThrows(CloudFileException.class, result);
        verifyNoInteractions(exceptionFactory);
        verify(dbRepository, times(1)).delete(cloudFile);
        verifyNoMoreInteractions(dbRepository);
        verify(eventPublisher, times(1)).publish(fileErrorCaptor.capture());
        verifyNoMoreInteractions(eventPublisher);
        DeleteCloudFileError fileDeleteError = fileErrorCaptor.getValue();
        assertNotNull(fileDeleteError);
        assertEquals(cloudFile, fileDeleteError.getCloudFile());
        assertEquals(request.getTraceId(), fileDeleteError.getTraceId());
        assertNotNull(fileDeleteError.getCreatedAt());
        assertEquals(cloudException.getMessage(), fileDeleteError.getErrorMessage());
    }
}