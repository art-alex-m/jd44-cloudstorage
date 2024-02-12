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
import ru.netology.cloudstorage.contracts.core.boundary.delete.DeleteCloudFileStorageDeleteActionRequest;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileException;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileExceptionCode;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileExceptionFactory;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.StorageFile;
import ru.netology.cloudstorage.contracts.event.exception.CloudstorageEventPublisherException;
import ru.netology.cloudstorage.contracts.event.handler.CloudstorageEventPublisher;
import ru.netology.cloudstorage.contracts.event.model.delete.DeleteCloudFileError;
import ru.netology.cloudstorage.contracts.event.model.delete.StorageFileDeleted;
import ru.netology.cloudstorage.contracts.storage.repository.DeleteCloudFileStorageRepository;
import ru.netology.cloudstorage.core.factory.CloudFileTestDataFactory;
import ru.netology.cloudstorage.core.factory.CoreCloudFileExceptionFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.verifyNoInteractions;
import static org.mockito.BDDMockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class CoreDeleteCloudFileStorageDeleteActionTest {

    @Mock
    DeleteCloudFileStorageRepository storageRepository;

    @Spy
    CloudFileExceptionFactory exceptionFactory = new CoreCloudFileExceptionFactory();

    @Mock
    CloudstorageEventPublisher eventPublisher;

    @Mock
    StorageFile storageFile;

    @Mock
    CloudFile cloudFile;

    @Mock
    DeleteCloudFileStorageDeleteActionRequest request;

    @InjectMocks
    CoreDeleteCloudFileStorageDeleteAction sut;

    @BeforeEach
    void setUp() {
        given(request.getTraceId()).willReturn(CloudFileTestDataFactory.getTraceId());
    }

    @Test
    void givenRequest_whenDelete_thenSuccess() {
        given(request.getStorageFile()).willReturn(storageFile);
        given(request.getCloudFile()).willReturn(cloudFile);
        ArgumentCaptor<StorageFileDeleted> fileDeletedCaptor = ArgumentCaptor.forClass(StorageFileDeleted.class);

        boolean result = sut.delete(request);

        assertTrue(result);
        verifyNoInteractions(exceptionFactory);
        verify(storageRepository, times(1)).delete(storageFile);
        verifyNoMoreInteractions(storageRepository);
        verify(eventPublisher, times(1)).publish(fileDeletedCaptor.capture());
        verifyNoMoreInteractions(eventPublisher);
        StorageFileDeleted fileDeleted = fileDeletedCaptor.getValue();
        assertNotNull(fileDeleted);
        assertEquals(cloudFile, fileDeleted.getCloudFile());
        assertEquals(request.getTraceId(), fileDeleted.getTraceId());
        assertNotNull(fileDeleted.getCreatedAt());
    }

    @Test
    void givenPublisherException_whenDelete_thenCloudException() {
        given(request.getStorageFile()).willReturn(storageFile);
        given(request.getCloudFile()).willReturn(cloudFile);
        ArgumentCaptor<StorageFileDeleted> fileDeletedCaptor = ArgumentCaptor.forClass(StorageFileDeleted.class);
        CloudstorageEventPublisherException publisherException = new CloudstorageEventPublisherException(
                "publisherException");
        given(eventPublisher.publish(any())).willThrow(publisherException);

        Executable result = () -> sut.delete(request);

        assertThrows(CloudFileException.class, result);
        verify(storageRepository, times(1)).delete(storageFile);
        verifyNoMoreInteractions(storageRepository);
        verify(eventPublisher, times(1)).publish(fileDeletedCaptor.capture());
        verifyNoMoreInteractions(eventPublisher);
        StorageFileDeleted fileDeleted = fileDeletedCaptor.getValue();
        assertNotNull(fileDeleted);
        assertEquals(cloudFile, fileDeleted.getCloudFile());
        assertEquals(request.getTraceId(), fileDeleted.getTraceId());
        assertNotNull(fileDeleted.getCreatedAt());
        verify(exceptionFactory, times(1))
                .create(CloudFileExceptionCode.STORAGE_FILE_DELETE_ERROR, request.getTraceId(), publisherException);
        verifyNoMoreInteractions(exceptionFactory);
    }

    @Test
    void givenRuntimeException_whenDelete_thenCloudException() {
        given(request.getStorageFile()).willReturn(storageFile);
        given(request.getCloudFile()).willReturn(cloudFile);
        RuntimeException runtimeException = new RuntimeException("runtimeException");
        given(storageRepository.delete(storageFile)).willThrow(runtimeException);
        ArgumentCaptor<DeleteCloudFileError> fileErrorCaptor = ArgumentCaptor.forClass(DeleteCloudFileError.class);

        Executable result = () -> sut.delete(request);

        assertThrows(CloudFileException.class, result);
        verify(storageRepository, times(1)).delete(storageFile);
        verifyNoMoreInteractions(storageRepository);
        verify(eventPublisher, times(1)).publish(fileErrorCaptor.capture());
        verifyNoMoreInteractions(eventPublisher);
        DeleteCloudFileError fileDeleteError = fileErrorCaptor.getValue();
        assertNotNull(fileDeleteError);
        assertEquals(cloudFile, fileDeleteError.getCloudFile());
        assertEquals(request.getTraceId(), fileDeleteError.getTraceId());
        assertNotNull(fileDeleteError.getCreatedAt());
        assertEquals(runtimeException.getMessage(), fileDeleteError.getErrorMessage());
        verify(exceptionFactory, times(1))
                .create(CloudFileExceptionCode.STORAGE_FILE_DELETE_ERROR, request.getTraceId(), runtimeException);
        verifyNoMoreInteractions(exceptionFactory);
    }

    @Test
    void givenCloudException_whenDelete_thenCloudException() {
        given(request.getStorageFile()).willReturn(storageFile);
        given(request.getCloudFile()).willReturn(cloudFile);
        CloudFileException cloudException = new CloudFileException(CloudFileExceptionCode.CLOUD_FILE_DELETE_ERROR,
                request.getTraceId());
        given(storageRepository.delete(storageFile)).willThrow(cloudException);
        ArgumentCaptor<DeleteCloudFileError> fileErrorCaptor = ArgumentCaptor.forClass(DeleteCloudFileError.class);

        Executable result = () -> sut.delete(request);

        assertThrows(CloudFileException.class, result);
        verifyNoInteractions(exceptionFactory);
        verify(storageRepository, times(1)).delete(storageFile);
        verifyNoMoreInteractions(storageRepository);
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
