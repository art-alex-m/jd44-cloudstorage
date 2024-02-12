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
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileReadyActionRequest;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileException;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileExceptionCode;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileExceptionFactory;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileStatusFactory;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatusCode;
import ru.netology.cloudstorage.contracts.core.model.CloudUser;
import ru.netology.cloudstorage.contracts.db.repository.CreateCloudFileInputDbRepository;
import ru.netology.cloudstorage.contracts.event.exception.CloudstorageEventPublisherException;
import ru.netology.cloudstorage.contracts.event.handler.CloudstorageEventPublisher;
import ru.netology.cloudstorage.contracts.event.model.CloudFileError;
import ru.netology.cloudstorage.contracts.event.model.create.CloudFileIsReady;
import ru.netology.cloudstorage.core.factory.CloudFileTestDataFactory;
import ru.netology.cloudstorage.core.factory.CoreCloudFileStatusFactory;
import ru.netology.cloudstorage.core.model.CoreCloudFile;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class CoreCreateCloudFileReadyActionTest {

    @Spy
    private final CloudFileStatusFactory statusFactory = new CoreCloudFileStatusFactory();

    @Mock
    private CreateCloudFileInputDbRepository dbRepository;

    @Mock
    private CloudFileExceptionFactory exceptionFactory;

    @Mock
    private CloudstorageEventPublisher eventPublisher;

    @Mock
    private CreateCloudFileReadyActionRequest request;

    @Mock
    private CloudUser cloudUser;

    private CloudFile cloudFile;

    @InjectMocks
    private CoreCreateCloudFileReadyAction sut;

    @BeforeEach
    void setUp() {
        cloudFile = CoreCloudFile.builder()
                .id(UUID.randomUUID())
                .user(cloudUser)
                .build();
        given(request.getTraceId()).willReturn(CloudFileTestDataFactory.getTraceId());
        given(request.getCloudFile()).willReturn(cloudFile);
    }

    @Test
    void givenRequest_whenUpdate_thenSuccess() {
        ArgumentCaptor<CloudFile> dbCloudFileCaptor = ArgumentCaptor.forClass(CloudFile.class);
        ArgumentCaptor<CloudFileIsReady> publisherCaptor = ArgumentCaptor.forClass(CloudFileIsReady.class);

        boolean result = sut.update(request);

        assertTrue(result);
        verify(dbRepository, times(1)).save(dbCloudFileCaptor.capture());
        verify(eventPublisher, times(1)).publish(publisherCaptor.capture());
        CloudFile dbCloudFile = dbCloudFileCaptor.getValue();
        CloudFileIsReady cloudFileIsReady = publisherCaptor.getValue();
        assertNotEquals(request.getCloudFile(), dbCloudFile);
        assertEquals(request.getCloudFile().getId(), dbCloudFile.getId());
        assertEquals(dbCloudFile, cloudFileIsReady.getCloudFile());
        assertNotNull(dbCloudFile.getStatus());
        assertEquals(CloudFileStatusCode.READY, dbCloudFile.getStatus().getCode());
        assertEquals(CloudFileTestDataFactory.getTraceId(), request.getTraceId());
        assertEquals(request.getTraceId(), dbCloudFile.getStatus().getTraceId());
        assertEquals(request.getTraceId(), cloudFileIsReady.getTraceId());
        assertNotNull(cloudFileIsReady.getCreatedAt());
    }

    @Test
    void givenRequest_whenUpdate_thenDbThrowException() {
        String exceptionMessage = "Db repository save exception";
        RuntimeException runtimeException = new RuntimeException(exceptionMessage);
        given(dbRepository.save(any(CloudFile.class))).willThrow(runtimeException);
        CloudFileException ex = mock(CloudFileException.class);
        given(exceptionFactory.create(CloudFileExceptionCode.DB_SAVE_IS_READY_ERROR,
                CloudFileTestDataFactory.getTraceId(), runtimeException)).willReturn(ex);
        ArgumentCaptor<CloudFileError> publisherCaptor = ArgumentCaptor.forClass(CloudFileError.class);

        Executable result = () -> sut.update(request);

        assertThrows(CloudFileException.class, result);
        verify(dbRepository, times(1)).save(any(CloudFile.class));
        verify(exceptionFactory, times(1))
                .create(CloudFileExceptionCode.DB_SAVE_IS_READY_ERROR, CloudFileTestDataFactory.getTraceId(),
                        runtimeException);
        verifyNoMoreInteractions(exceptionFactory);
        verify(eventPublisher, times(1))
                .publish(publisherCaptor.capture());
        verifyNoMoreInteractions(eventPublisher);
        CloudFileError cloudFileError = publisherCaptor.getValue();
        assertNotNull(cloudFileError);
        assertEquals(exceptionMessage, cloudFileError.getErrorMessage());
        assertNotNull(request.getCloudFile());
        assertEquals(request.getCloudFile(), cloudFileError.getCloudFile());
        assertNotNull(cloudFileError.getCreatedAt());
    }

    @Test
    void givenRequest_whenUpdate_thenEventPublisherThrowException() {
        CloudstorageEventPublisherException publisherException = mock(
                CloudstorageEventPublisherException.class);
        given(eventPublisher.publish(any())).willThrow(publisherException);
        CloudFileException ex = mock(CloudFileException.class);
        given(exceptionFactory.create(CloudFileExceptionCode.DB_SAVE_IS_READY_ERROR,
                CloudFileTestDataFactory.getTraceId(), publisherException)).willReturn(ex);

        Executable result = () -> sut.update(request);

        assertThrows(CloudFileException.class, result);
        verify(dbRepository, times(1)).save(any(CloudFile.class));
        verify(exceptionFactory, times(1))
                .create(CloudFileExceptionCode.DB_SAVE_IS_READY_ERROR, CloudFileTestDataFactory.getTraceId(),
                        publisherException);
        verifyNoMoreInteractions(exceptionFactory);
        verify(eventPublisher, times(1)).publish(any(CloudFileIsReady.class));
        verifyNoMoreInteractions(eventPublisher);
    }
}
