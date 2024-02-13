package ru.netology.cloudstorage.core.boundary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.netology.cloudstorage.contracts.core.boundary.CloudFileErrorStatusActionRequest;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileException;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileExceptionCode;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileExceptionFactory;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileStatusFactory;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatusCode;
import ru.netology.cloudstorage.contracts.core.model.CloudUser;
import ru.netology.cloudstorage.contracts.core.model.StorageFile;
import ru.netology.cloudstorage.contracts.db.repository.CloudFileErrorStatusDbRepository;
import ru.netology.cloudstorage.core.factory.CloudFileTestDataFactory;
import ru.netology.cloudstorage.core.factory.CoreCloudFileExceptionFactory;
import ru.netology.cloudstorage.core.factory.CoreCloudFileStatusFactory;
import ru.netology.cloudstorage.core.model.CoreCloudFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
class CoreCloudFileErrorStatusActionTest {

    @Mock
    CloudFileErrorStatusDbRepository dbRepository;

    @Spy
    CloudFileStatusFactory statusFactory = new CoreCloudFileStatusFactory();

    @Spy
    CloudFileExceptionFactory exceptionFactory = new CoreCloudFileExceptionFactory();

    @Mock
    CloudFileErrorStatusActionRequest request;

    @Mock
    StorageFile storageFile;

    @Mock
    CloudUser cloudUser;

    @InjectMocks
    CoreCloudFileErrorStatusAction sut;

    CloudFile cloudFile;

    @BeforeEach
    void setUp() {
        cloudFile = CoreCloudFile.builder()
                .user(cloudUser)
                .id(CloudFileTestDataFactory.testUuid)
                .status(statusFactory.create(CloudFileStatusCode.READY, CloudFileTestDataFactory.traceId))
                .storageFile(storageFile)
                .fileName(CloudFileTestDataFactory.testFileName)
                .build();
        given(request.getTraceId()).willReturn(CloudFileTestDataFactory.traceId);
        given(request.getCloudFile()).willReturn(cloudFile);
        given(request.getErrorMessage()).willReturn("Error message");
    }

    @Test
    void givenRequest_whenUpdate_thenSuccess() {
        ArgumentCaptor<CloudFile> updatedFileCaptor = ArgumentCaptor.forClass(CloudFile.class);
        given(dbRepository.save(any(CloudFile.class))).willReturn(true);

        boolean result = sut.update(request);

        assertTrue(result);
        verifyNoInteractions(exceptionFactory);
        verify(dbRepository, times(1)).save(updatedFileCaptor.capture());
        CloudFile updatedFile = updatedFileCaptor.getValue();
        assertNotNull(updatedFile);
        assertNotEquals(cloudFile.getStatus(), updatedFile.getStatus());
        assertEquals(CloudFileStatusCode.ERROR, updatedFile.getStatus().getCode());
        assertEquals("Error message", updatedFile.getStatus().getMessage());
        assertEquals(CloudFileTestDataFactory.traceId, updatedFile.getStatus().getTraceId());
        assertEquals(cloudFile.getId(), updatedFile.getId());
        assertEquals(cloudFile.getStorageFile(), updatedFile.getStorageFile());
        assertEquals(cloudFile.getUpdatedAt(), updatedFile.getUpdatedAt());
        assertEquals(cloudFile.getUser(), updatedFile.getUser());
        assertEquals(cloudFile.getCreatedAt(), updatedFile.getCreatedAt());
        assertEquals(cloudFile.getFileName(), updatedFile.getFileName());
    }

    @Test
    void givenRuntimeException_whenUpdate_thenCloudFileException() {
        RuntimeException runtimeException = new RuntimeException("Runtime exception");
        given(dbRepository.save(any(CloudFile.class))).willThrow(runtimeException);

        Executable result = () -> sut.update(request);

        assertThrows(CloudFileException.class, result);
        verify(dbRepository, times(1)).save(any(CloudFile.class));
        verifyNoMoreInteractions(dbRepository);
        verify(exceptionFactory, times(1))
                .create(CloudFileExceptionCode.DB_SAVE_STATUS_ERROR, CloudFileTestDataFactory.traceId,
                        runtimeException);
        verifyNoMoreInteractions(exceptionFactory);
    }
}
