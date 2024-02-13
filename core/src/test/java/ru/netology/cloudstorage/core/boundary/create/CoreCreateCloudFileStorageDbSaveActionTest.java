package ru.netology.cloudstorage.core.boundary.create;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileStorageDbSaveActionRequest;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileException;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileExceptionCode;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileExceptionFactory;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.db.repository.CreateCloudFileInputDbRepository;
import ru.netology.cloudstorage.contracts.event.exception.CloudstorageEventPublisherException;
import ru.netology.cloudstorage.contracts.event.handler.CloudstorageEventPublisher;
import ru.netology.cloudstorage.contracts.event.model.create.CreateCloudFileError;
import ru.netology.cloudstorage.contracts.event.model.create.StorageFileDbStored;
import ru.netology.cloudstorage.core.factory.CloudFileTestDataFactory;

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
class CoreCreateCloudFileStorageDbSaveActionTest {

    @Mock
    CloudFileException cloudFileException;

    @Mock
    CloudFile cloudFile;

    @Mock
    CreateCloudFileInputDbRepository dbRepository;

    @Mock
    CloudFileExceptionFactory exceptionFactory;

    @Mock
    CloudstorageEventPublisher eventPublisher;

    @Mock
    CreateCloudFileStorageDbSaveActionRequest request;

    @InjectMocks
    CoreCreateCloudFileStorageDbSaveAction sut;

    @BeforeEach
    void setUp() {
        given(request.getCloudFile()).willReturn(cloudFile);
        given(request.getTraceId()).willReturn(CloudFileTestDataFactory.getTraceId());
    }

    @Test
    void givenRequest_whenSave_thenSuccess() {
        ArgumentCaptor<StorageFileDbStored> fileDbStoredCaptor = ArgumentCaptor.forClass(StorageFileDbStored.class);

        boolean result = sut.save(request);

        assertTrue(result);
        verify(dbRepository, times(1)).save(cloudFile);
        verify(eventPublisher, times(1)).publish(fileDbStoredCaptor.capture());
        verifyNoMoreInteractions(eventPublisher);
        verifyNoInteractions(exceptionFactory);
        StorageFileDbStored storedEvent = fileDbStoredCaptor.getValue();
        assertNotNull(storedEvent);
        assertEquals(cloudFile, storedEvent.getCloudFile());
        assertEquals(CloudFileTestDataFactory.getTraceId(), storedEvent.getTraceId());
        assertNotNull(storedEvent.getCreatedAt());
    }

    @Test
    void givenPublisherException_whenSave_thenDbFileSaveErrorCloudFileException() {
        CloudstorageEventPublisherException publisherException = new CloudstorageEventPublisherException(
                "test message");
        given(eventPublisher.publish(any(StorageFileDbStored.class))).willThrow(publisherException);
        given(exceptionFactory
                .create(CloudFileExceptionCode.DB_SAVE_STORAGE_FILE_ERROR, CloudFileTestDataFactory.getTraceId(),
                        publisherException))
                .willThrow(cloudFileException);

        Executable result = () -> sut.save(request);

        assertThrows(CloudFileException.class, result);
        verify(dbRepository, times(1)).save(cloudFile);
        verify(eventPublisher, times(1)).publish(any(StorageFileDbStored.class));
        verify(exceptionFactory, times(1))
                .create(CloudFileExceptionCode.DB_SAVE_STORAGE_FILE_ERROR, CloudFileTestDataFactory.getTraceId(),
                        publisherException);
        verifyNoMoreInteractions(eventPublisher);
        verifyNoMoreInteractions(exceptionFactory);
    }

    @Test
    void givenException_whenSave_thenDbFileSaveCloudFileExceptionAndPublishCloudFileErrorEvent() {
        RuntimeException runtimeException = new RuntimeException("Test exception");
        given(dbRepository.save(cloudFile)).willThrow(runtimeException);
        given(exceptionFactory
                .create(CloudFileExceptionCode.DB_SAVE_STORAGE_FILE_ERROR, CloudFileTestDataFactory.getTraceId(),
                        runtimeException))
                .willThrow(cloudFileException);
        ArgumentCaptor<CreateCloudFileError> fileDbStoredCaptor = ArgumentCaptor.forClass(CreateCloudFileError.class);

        Executable result = () -> sut.save(request);

        assertThrows(CloudFileException.class, result);
        verify(dbRepository, times(1)).save(cloudFile);
        verify(eventPublisher, times(1)).publish(fileDbStoredCaptor.capture());
        verify(exceptionFactory, times(1))
                .create(CloudFileExceptionCode.DB_SAVE_STORAGE_FILE_ERROR, CloudFileTestDataFactory.getTraceId(),
                        runtimeException);
        verifyNoMoreInteractions(eventPublisher);
        verifyNoMoreInteractions(exceptionFactory);
        CreateCloudFileError fileErrorEvent = fileDbStoredCaptor.getValue();
        assertNotNull(fileErrorEvent);
        assertNotNull(fileErrorEvent.getCreatedAt());
        assertEquals(CloudFileTestDataFactory.getTraceId(), fileErrorEvent.getTraceId());
        assertEquals(runtimeException.getMessage(), fileErrorEvent.getErrorMessage());
        assertEquals(cloudFile, fileErrorEvent.getCloudFile());
    }
}
