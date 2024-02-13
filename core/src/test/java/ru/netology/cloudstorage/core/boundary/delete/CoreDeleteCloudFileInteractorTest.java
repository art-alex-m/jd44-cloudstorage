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
import ru.netology.cloudstorage.contracts.core.boundary.delete.DeleteCloudFileInputRequest;
import ru.netology.cloudstorage.contracts.core.boundary.delete.DeleteCloudFileInputResponse;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileException;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileExceptionCode;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileExceptionFactory;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileStatusFactory;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatusCode;
import ru.netology.cloudstorage.contracts.core.model.CloudUser;
import ru.netology.cloudstorage.contracts.core.model.StorageFile;
import ru.netology.cloudstorage.contracts.db.repository.DeleteCloudFileInputDbRepository;
import ru.netology.cloudstorage.contracts.event.exception.CloudstorageEventPublisherException;
import ru.netology.cloudstorage.contracts.event.handler.CloudstorageEventPublisher;
import ru.netology.cloudstorage.contracts.event.model.delete.DeleteCloudFileError;
import ru.netology.cloudstorage.contracts.event.model.delete.StorageFileDbDeleted;
import ru.netology.cloudstorage.core.factory.CloudFileTestDataFactory;
import ru.netology.cloudstorage.core.factory.CoreCloudFileExceptionFactory;
import ru.netology.cloudstorage.core.factory.CoreCloudFileStatusFactory;
import ru.netology.cloudstorage.core.model.CoreCloudFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.verifyNoInteractions;
import static org.mockito.BDDMockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class CoreDeleteCloudFileInteractorTest {

    @Mock
    DeleteCloudFileInputDbRepository dbRepository;

    @Spy
    CloudFileStatusFactory statusFactory = new CoreCloudFileStatusFactory();

    @Spy
    CloudFileExceptionFactory exceptionFactory = new CoreCloudFileExceptionFactory();

    @Mock
    CloudstorageEventPublisher eventPublisher;

    @Mock
    DeleteCloudFileInputRequest request;

    CloudFile cloudFile;

    @Mock
    StorageFile storageFile;

    @Mock
    CloudUser cloudUser;

    @InjectMocks
    CoreDeleteCloudFileInteractor sut;

    @BeforeEach
    void setUp() {
        given(request.getUser()).willReturn(cloudUser);
        given(request.getFileName()).willReturn(CloudFileTestDataFactory.testFileName);
        given(request.getTraceId()).willReturn(CloudFileTestDataFactory.traceId);
        cloudFile = CoreCloudFile.builder()
                .user(cloudUser)
                .id(CloudFileTestDataFactory.testUuid)
                .status(statusFactory.create(CloudFileStatusCode.READY, CloudFileTestDataFactory.traceId))
                .storageFile(storageFile)
                .fileName(CloudFileTestDataFactory.testFileName)
                .build();
    }

    @Test
    void givenRequest_whenDelete_thenSuccess() {
        given(dbRepository.findByUserAndFileNameAndReadyStatus(cloudUser, request.getFileName()))
                .willReturn(Optional.of(cloudFile));
        ArgumentCaptor<StorageFileDbDeleted> fileDeletedCaptor = ArgumentCaptor.forClass(StorageFileDbDeleted.class);
        ArgumentCaptor<CloudFile> updatedCloudCaptor = ArgumentCaptor.forClass(CloudFile.class);

        DeleteCloudFileInputResponse result = sut.delete(request);

        assertNotNull(result);
        verifyNoInteractions(exceptionFactory);
        verify(dbRepository, times(1)).delete(storageFile);
        verify(dbRepository, times(1)).update(updatedCloudCaptor.capture());
        verifyNoMoreInteractions(dbRepository);
        CloudFile updatedFile = updatedCloudCaptor.getValue();
        assertNull(updatedFile.getStorageFile());
        assertEquals(CloudFileStatusCode.DELETED, updatedFile.getStatus().getCode());
        assertEquals(CloudFileTestDataFactory.traceId, updatedFile.getStatus().getTraceId());
        verify(eventPublisher, times(1)).publish(fileDeletedCaptor.capture());
        verifyNoMoreInteractions(eventPublisher);
        StorageFileDbDeleted fileDeleted = fileDeletedCaptor.getValue();
        assertNotNull(fileDeleted);
        assertNotEquals(cloudFile, fileDeleted.getCloudFile());
        assertEquals(updatedFile, fileDeleted.getCloudFile());
        assertEquals(storageFile, fileDeleted.getStorageFile());
        assertEquals(request.getTraceId(), fileDeleted.getTraceId());
        assertNotNull(fileDeleted.getCreatedAt());
    }

    @Test
    void givenDbEmpty_whenDelete_thenCloudFileException() {
        given(dbRepository.findByUserAndFileNameAndReadyStatus(cloudUser, request.getFileName()))
                .willReturn(Optional.empty());
        ArgumentCaptor<DeleteCloudFileError> fileErrorCaptor = ArgumentCaptor.forClass(DeleteCloudFileError.class);

        Executable result = () -> sut.delete(request);

        assertThrows(CloudFileException.class, result);
        verify(dbRepository, times(1)).findByUserAndFileNameAndReadyStatus(cloudUser,
                CloudFileTestDataFactory.testFileName);
        verifyNoMoreInteractions(dbRepository);
        verify(exceptionFactory, times(1))
                .create(CloudFileExceptionCode.FILE_NOT_FOUND_ERROR, CloudFileTestDataFactory.traceId);
        verifyNoMoreInteractions(exceptionFactory);
        verify(eventPublisher, times(1)).publish(fileErrorCaptor.capture());
        DeleteCloudFileError fileError = fileErrorCaptor.getValue();
        assertNotNull(fileError);
        assertEquals(CloudFileTestDataFactory.traceId, fileError.getTraceId());
        assertEquals(CloudFileExceptionCode.FILE_NOT_FOUND_ERROR.code, fileError.getErrorMessage());
        assertNull(fileError.getCloudFile());
        assertNotNull(fileError.getCreatedAt());
    }

    @Test
    void givenSomeException_whenDelete_thenCloudFileException() {
        given(dbRepository.findByUserAndFileNameAndReadyStatus(cloudUser, request.getFileName()))
                .willReturn(Optional.of(cloudFile));
        ArgumentCaptor<DeleteCloudFileError> fileErrorCaptor = ArgumentCaptor.forClass(DeleteCloudFileError.class);
        RuntimeException runtimeException = new RuntimeException("Runtime exception");
        given(dbRepository.delete(any(StorageFile.class))).willThrow(runtimeException);

        Executable result = () -> sut.delete(request);

        assertThrows(CloudFileException.class, result);
        verify(dbRepository, times(1)).findByUserAndFileNameAndReadyStatus(cloudUser,
                CloudFileTestDataFactory.testFileName);
        verify(dbRepository, times(1)).delete(storageFile);
        verifyNoMoreInteractions(dbRepository);
        verify(exceptionFactory, times(1))
                .create(CloudFileExceptionCode.DB_STORAGE_FILE_DELETE_ERROR, CloudFileTestDataFactory.traceId,
                        runtimeException);
        verifyNoMoreInteractions(exceptionFactory);
        verify(eventPublisher, times(1)).publish(fileErrorCaptor.capture());
        DeleteCloudFileError fileError = fileErrorCaptor.getValue();
        assertNotNull(fileError);
        assertEquals(CloudFileTestDataFactory.traceId, fileError.getTraceId());
        assertEquals(runtimeException.getMessage(), fileError.getErrorMessage());
        assertEquals(cloudFile, fileError.getCloudFile());
        assertNotNull(fileError.getCreatedAt());
    }

    @Test
    void givenPublisherException_whenDelete_thenCloudFileException() {
        given(dbRepository.findByUserAndFileNameAndReadyStatus(cloudUser, request.getFileName()))
                .willReturn(Optional.of(cloudFile));
        ArgumentCaptor<StorageFileDbDeleted> fileDeletedCaptor = ArgumentCaptor.forClass(StorageFileDbDeleted.class);
        ArgumentCaptor<CloudFile> updatedCloudCaptor = ArgumentCaptor.forClass(CloudFile.class);
        CloudstorageEventPublisherException publisherException = new CloudstorageEventPublisherException(
                "Publisher exception");
        given(eventPublisher.publish(any(StorageFileDbDeleted.class))).willThrow(publisherException);

        Executable result = () -> sut.delete(request);

        assertThrows(CloudFileException.class, result);
        verify(dbRepository, times(1)).delete(storageFile);
        verify(dbRepository, times(1)).update(updatedCloudCaptor.capture());
        verifyNoMoreInteractions(dbRepository);
        CloudFile updatedFile = updatedCloudCaptor.getValue();
        assertNull(updatedFile.getStorageFile());
        assertEquals(CloudFileStatusCode.DELETED, updatedFile.getStatus().getCode());
        assertEquals(CloudFileTestDataFactory.traceId, updatedFile.getStatus().getTraceId());
        verify(eventPublisher, times(1)).publish(fileDeletedCaptor.capture());
        verifyNoMoreInteractions(eventPublisher);
        StorageFileDbDeleted fileDeleted = fileDeletedCaptor.getValue();
        assertNotNull(fileDeleted);
        assertNotEquals(cloudFile, fileDeleted.getCloudFile());
        assertEquals(updatedFile, fileDeleted.getCloudFile());
        assertEquals(storageFile, fileDeleted.getStorageFile());
        assertEquals(request.getTraceId(), fileDeleted.getTraceId());
        assertNotNull(fileDeleted.getCreatedAt());
        verify(exceptionFactory, times(1))
                .create(CloudFileExceptionCode.DB_STORAGE_FILE_DELETE_ERROR, CloudFileTestDataFactory.traceId,
                        publisherException);
        verifyNoMoreInteractions(exceptionFactory);
    }
}