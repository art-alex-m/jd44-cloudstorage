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
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileInputRequest;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileInputResponse;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileException;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileExceptionCode;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileExceptionFactory;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileIdFactory;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileStatusFactory;
import ru.netology.cloudstorage.contracts.core.factory.CreateCloudFileInputResponseFactory;
import ru.netology.cloudstorage.contracts.core.model.*;
import ru.netology.cloudstorage.contracts.db.repository.CreateCloudFileInputDbRepository;
import ru.netology.cloudstorage.contracts.event.handler.CloudstorageEventPublisher;
import ru.netology.cloudstorage.contracts.event.model.create.CloudFileDbCreated;
import ru.netology.cloudstorage.core.factory.CoreCloudFileIdFactory;
import ru.netology.cloudstorage.core.factory.CoreCloudFileStatusFactory;
import ru.netology.cloudstorage.core.model.CoreTraceId;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CoreCreateCloudFileInteractorTest {

    @Spy
    private final CloudFileIdFactory idFactory = new CoreCloudFileIdFactory();

    @Spy
    private final CloudFileStatusFactory statusFactory = new CoreCloudFileStatusFactory();

    private final UUID testUuid = UUID.fromString("6040a95b-8787-4bdb-b711-36d9d413be5c");

    private final TraceId traceId = new CoreTraceId(4915623779632245515L, testUuid);

    private final String testFileName = "some test name.pdf";

    @Mock
    private CreateCloudFileInputResponse response;

    @Mock
    private CreateCloudFileInputRequest request;

    @Mock
    private CloudUser cloudUser;

    @Mock
    private FileResource fileResource;

    @Mock
    private CreateCloudFileInputResponseFactory responseFactory;

    @Mock
    private CreateCloudFileInputDbRepository dbRepository;

    @Mock
    private CloudstorageEventPublisher eventPublisher;

    @Mock
    private CloudFileExceptionFactory exceptionFactory;

    @InjectMocks
    private CoreCreateCloudFileInteractor sut;

    @BeforeEach
    void setUp() {
        given(cloudUser.getId()).willReturn(testUuid);
        given(fileResource.getFileName()).willReturn(testFileName);
        given(request.getTraceId()).willReturn(traceId);
        given(request.getUser()).willReturn(cloudUser);
        given(request.getUserFile()).willReturn(fileResource);
    }

    @Test
    void givenGoodRequest_whenCreate_thenSuccess() {
        given(dbRepository.save(any(CloudFile.class))).willReturn(true);
        ArgumentCaptor<CloudFile> dbCloudFileCaptor = ArgumentCaptor.forClass(CloudFile.class);
        given(responseFactory.create(any(CloudFile.class))).willReturn(response);
        ArgumentCaptor<CloudFile> responseCloudFileCaptor = ArgumentCaptor.forClass(CloudFile.class);
        ArgumentCaptor<CloudFileDbCreated> eventCaptor = ArgumentCaptor.forClass(CloudFileDbCreated.class);

        CreateCloudFileInputResponse result = sut.create(request);

        assertEquals(response, result);
        verify(dbRepository, times(1)).save(dbCloudFileCaptor.capture());
        verify(responseFactory, times(1)).create(responseCloudFileCaptor.capture());
        verify(eventPublisher, times(1)).publish(eventCaptor.capture());
        verifyNoInteractions(exceptionFactory);
        CloudFile dbCloudFile = dbCloudFileCaptor.getValue();
        CloudFile responseCloudFile = responseCloudFileCaptor.getValue();
        assertEquals(traceId, request.getTraceId());
        assertEquals(cloudUser, request.getUser());
        assertEquals(dbCloudFile, responseCloudFile);
        assertEquals(request.getUser(), dbCloudFile.getUser());
        assertEquals(testFileName, dbCloudFile.getFileName());
        assertNotNull(dbCloudFile.getStatus());
        assertEquals(CloudFileStatusCode.LOADING, dbCloudFile.getStatus().getCode());
        assertEquals(request.getTraceId(), dbCloudFile.getStatus().getTraceId());
        assertNull(dbCloudFile.getStorageFile());
        assertNotNull(dbCloudFile.getCreatedAt());
        assertNotNull(dbCloudFile.getUpdatedAt());
        CloudFileDbCreated eventDbCreated = eventCaptor.getValue();
        assertNotNull(eventDbCreated);
        assertEquals(dbCloudFile, eventDbCreated.getCloudFile());
        assertEquals(request.getTraceId(), eventDbCreated.getTraceId());
        assertEquals(request.getUserFile(), eventDbCreated.getUserFile());
        assertNotNull(eventDbCreated.getCreatedAt());
    }

    @Test
    void givenGoodRequest_whenCreate_thenDbCreateErrorWithException() {
        RuntimeException runtimeException = new RuntimeException("Db repository save exception");
        given(dbRepository.save(any(CloudFile.class))).willThrow(runtimeException);
        CloudFileException ex = mock(CloudFileException.class);
        given(exceptionFactory.create(CloudFileExceptionCode.DB_CREATE_ERROR, traceId, runtimeException)).willReturn(
                ex);

        Executable result = () -> sut.create(request);

        assertThrows(CloudFileException.class, result);
        verify(dbRepository, times(1)).save(any(CloudFile.class));
        verify(exceptionFactory, times(1))
                .create(CloudFileExceptionCode.DB_CREATE_ERROR, traceId, runtimeException);
        verifyNoMoreInteractions(exceptionFactory);
        verifyNoInteractions(eventPublisher);
        verifyNoInteractions(responseFactory);
    }

    @Test
    void givenGoodRequest_whenCreate_thenEventPublishError() {
        given(dbRepository.save(any(CloudFile.class))).willReturn(true);
        RuntimeException runtimeException = new RuntimeException("Event publisher error");
        CloudFileException ex = mock(CloudFileException.class);
        given(eventPublisher.publish(any(CloudFileDbCreated.class))).willThrow(runtimeException);
        given(exceptionFactory.create(CloudFileExceptionCode.DB_CREATE_ERROR, traceId,
                runtimeException)).willReturn(ex);

        Executable result = () -> sut.create(request);

        assertThrows(CloudFileException.class, result);
        verify(dbRepository, times(1)).save(any(CloudFile.class));
        verify(exceptionFactory, times(1)).create(CloudFileExceptionCode.DB_CREATE_ERROR, traceId, runtimeException);
        verifyNoMoreInteractions(exceptionFactory);
        verify(eventPublisher, times(1)).publish(any(CloudFileDbCreated.class));
        verifyNoMoreInteractions(eventPublisher);
        verifyNoInteractions(responseFactory);
    }
}
