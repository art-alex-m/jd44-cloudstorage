package ru.netology.cloudstorage.core.boundary.create;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileStorageUploadActionRequest;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileException;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileExceptionCode;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileExceptionFactory;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileStatusFactory;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatusCode;
import ru.netology.cloudstorage.contracts.core.model.FileResource;
import ru.netology.cloudstorage.contracts.core.model.StorageFile;
import ru.netology.cloudstorage.contracts.event.exception.CloudstorageEventPublisherException;
import ru.netology.cloudstorage.contracts.event.handler.CloudstorageEventPublisher;
import ru.netology.cloudstorage.contracts.event.model.create.CreateCloudFileError;
import ru.netology.cloudstorage.contracts.event.model.create.StorageFileUploaded;
import ru.netology.cloudstorage.contracts.storage.repository.CreateCloudFileStorageUploadRepository;
import ru.netology.cloudstorage.core.factory.CloudFileTestDataFactory;
import ru.netology.cloudstorage.core.factory.CoreCloudFileStatusFactory;
import ru.netology.cloudstorage.core.model.CoreCloudFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;


@ExtendWith(MockitoExtension.class)
class CoreCreateCloudFileStorageUploadActionTest {

    CloudFileTestDataFactory testFactory = new CloudFileTestDataFactory();

    @Mock
    CloudFileException cloudFileException;

    CloudFile cloudFile;

    @Mock
    FileResource fileResource;

    @Mock
    StorageFile storageFile;

    @Mock
    CreateCloudFileStorageUploadRepository uploadRepository;

    @Spy
    CloudFileStatusFactory statusFactory = new CoreCloudFileStatusFactory();

    @Mock
    CloudFileExceptionFactory exceptionFactory;

    @Mock
    CloudstorageEventPublisher eventPublisher;

    @Mock
    CreateCloudFileStorageUploadActionRequest request;

    @InjectMocks
    CoreCreateCloudFileStorageUploadAction sut;

    @BeforeEach
    void setUp() {
        cloudFile = CoreCloudFile.builder()
                .id(testFactory.getTestUuid())
                .createdAt(testFactory.getTestInstant())
                .updatedAt(testFactory.getTestInstant())
                .build();
        given(request.getCloudFile()).willReturn(cloudFile);
        given(request.getUserFile()).willReturn(fileResource);
        given(request.getTraceId()).willReturn(testFactory.getTraceId());
    }

    @Test
    void givenRequest_whenUpload_thenSuccess() {
        given(uploadRepository.save(fileResource)).willReturn(storageFile);
        ArgumentCaptor<StorageFileUploaded> uploadedCaptor = ArgumentCaptor.forClass(StorageFileUploaded.class);

        boolean result = sut.upload(request);

        assertTrue(result);
        verify(eventPublisher, times(1)).publish(uploadedCaptor.capture());
        verify(uploadRepository, times(1)).save(fileResource);
        verifyNoInteractions(exceptionFactory);
        verifyNoMoreInteractions(eventPublisher);
        StorageFileUploaded uploadedEvent = uploadedCaptor.getValue();
        assertNotNull(uploadedEvent);
        assertEquals(testFactory.getTraceId(), uploadedEvent.getTraceId());
        assertNotNull(uploadedEvent.getCreatedAt());
        assertNotEquals(cloudFile, uploadedEvent.getCloudFile());
        assertEquals(cloudFile.getId(), uploadedEvent.getCloudFile().getId());
        assertEquals(cloudFile.getCreatedAt(), uploadedEvent.getCloudFile().getCreatedAt());
        assertEquals(cloudFile.getUpdatedAt(), uploadedEvent.getCloudFile().getUpdatedAt());
        assertEquals(CloudFileStatusCode.UPLOADED, uploadedEvent.getCloudFile().getStatus().getCode());
        assertEquals(testFactory.getTraceId(), uploadedEvent.getCloudFile().getStatus().getTraceId());
        assertEquals(storageFile, uploadedEvent.getCloudFile().getStorageFile());
    }

    @Test
    void givenException_whenUpload_thenCloudFileErrorEventAndUploadErrorException() {
        RuntimeException runtimeException = new RuntimeException("Any upload error");
        given(uploadRepository.save(fileResource)).willThrow(runtimeException);
        given(exceptionFactory
                .create(CloudFileExceptionCode.UPLOAD_ERROR, testFactory.getTraceId(), runtimeException))
                .willThrow(cloudFileException);
        ArgumentCaptor<CreateCloudFileError> fileErrorEventCaptor = ArgumentCaptor.forClass(CreateCloudFileError.class);

        Executable result = () -> sut.upload(request);

        assertThrows(CloudFileException.class, result);
        verify(eventPublisher, times(1)).publish(fileErrorEventCaptor.capture());
        verify(exceptionFactory, times(1))
                .create(CloudFileExceptionCode.UPLOAD_ERROR, testFactory.getTraceId(), runtimeException);
        CreateCloudFileError fileErrorEvent = fileErrorEventCaptor.getValue();
        assertNotNull(fileErrorEvent);
        assertEquals(runtimeException.getMessage(), fileErrorEvent.getErrorMessage());
        assertEquals(request.getTraceId(), fileErrorEvent.getTraceId());
        assertEquals(request.getCloudFile(), fileErrorEvent.getCloudFile());
        verifyNoMoreInteractions(eventPublisher);
        verifyNoMoreInteractions(exceptionFactory);
        verifyNoMoreInteractions(uploadRepository);
    }

    @Test
    void givenEventPublisherException_whenUpload_thenUploadCloudFileException() {
        CloudstorageEventPublisherException publisherException = new CloudstorageEventPublisherException(
                "test message");
        given(eventPublisher.publish(any(StorageFileUploaded.class))).willThrow(publisherException);
        given(exceptionFactory
                .create(CloudFileExceptionCode.UPLOAD_ERROR, testFactory.getTraceId(), publisherException))
                .willThrow(cloudFileException);

        Executable result = () -> sut.upload(request);

        assertThrows(CloudFileException.class, result);
        verify(uploadRepository, times(1)).save(fileResource);
        verify(eventPublisher, times(1)).publish(any(StorageFileUploaded.class));
        verify(exceptionFactory, times(1))
                .create(CloudFileExceptionCode.UPLOAD_ERROR, testFactory.getTraceId(), publisherException);
        verifyNoMoreInteractions(eventPublisher);
        verifyNoMoreInteractions(exceptionFactory);
    }
}
