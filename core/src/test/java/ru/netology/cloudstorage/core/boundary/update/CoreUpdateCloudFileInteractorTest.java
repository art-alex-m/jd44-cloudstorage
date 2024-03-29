package ru.netology.cloudstorage.core.boundary.update;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.netology.cloudstorage.contracts.core.boundary.update.UpdateCloudFileInputRequest;
import ru.netology.cloudstorage.contracts.core.boundary.update.UpdateCloudFileInputResponse;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileException;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileExceptionCode;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileExceptionFactory;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatus;
import ru.netology.cloudstorage.contracts.core.model.CloudUser;
import ru.netology.cloudstorage.contracts.db.repository.UpdateCloudFileInputDbRepository;
import ru.netology.cloudstorage.contracts.event.exception.CloudstorageEventPublisherException;
import ru.netology.cloudstorage.contracts.event.handler.CloudstorageEventPublisher;
import ru.netology.cloudstorage.contracts.event.model.update.CloudFileUpdated;
import ru.netology.cloudstorage.contracts.event.model.update.UpdateCloudFileError;
import ru.netology.cloudstorage.core.factory.CloudFileTestDataFactory;
import ru.netology.cloudstorage.core.factory.CoreCloudFileExceptionFactory;
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
class CoreUpdateCloudFileInteractorTest {

    static final String newFileName = "new-file-name.xls";

    @Mock
    UpdateCloudFileInputRequest request;

    @Mock
    CloudFileStatus cloudFileStatus;

    @Mock
    CloudUser cloudUser;

    @Mock
    CloudFile cloudFile;

    @Mock
    UpdateCloudFileInputDbRepository dbRepository;

    @Mock
    CloudstorageEventPublisher eventPublisher;

    @Spy
    CloudFileExceptionFactory exceptionFactory = new CoreCloudFileExceptionFactory();

    @InjectMocks
    CoreUpdateCloudFileInteractor sut;

    @BeforeEach
    void setUp() {
        given(request.getUser()).willReturn(cloudUser);
        given(request.getTraceId()).willReturn(CloudFileTestDataFactory.getTraceId());
        given(request.getNewFileName()).willReturn(newFileName);
    }

    @Test
    void givenGoodRequest_whenUpdate_thenSuccess() {
        given(request.getFileName()).willReturn(CloudFileTestDataFactory.getTestFileName());
        given(dbRepository.uniqueNewName(cloudUser, newFileName)).willReturn(true);
        given(cloudFile.getId()).willReturn(CloudFileTestDataFactory.getTestUuid());
        given(cloudFile.getStatus()).willReturn(cloudFileStatus);
        given(dbRepository.findByUserAndFileName(cloudUser, CloudFileTestDataFactory.getTestFileName()))
                .willReturn(Optional.of(cloudFile));
        ArgumentCaptor<CoreCloudFile> updatedFileCaptor = ArgumentCaptor.forClass(CoreCloudFile.class);
        ArgumentCaptor<CloudFileUpdated> updatedEventCaptor = ArgumentCaptor.forClass(CloudFileUpdated.class);

        UpdateCloudFileInputResponse result = sut.update(request);

        assertNotNull(result);
        assertEquals(CloudFileTestDataFactory.getTestUuid(), result.getCloudFileId());
        assertEquals(cloudFileStatus, result.getCloudFileStatus());
        verify(dbRepository, times(1)).uniqueNewName(cloudUser, newFileName);
        verify(dbRepository, times(1)).findByUserAndFileName(cloudUser, CloudFileTestDataFactory.getTestFileName());
        verify(dbRepository, times(1)).update(updatedFileCaptor.capture());
        verify(eventPublisher, times(1)).publish(updatedEventCaptor.capture());
        verifyNoInteractions(exceptionFactory);
        CoreCloudFile updatedFile = updatedFileCaptor.getValue();
        assertNotNull(updatedFile);
        assertEquals(cloudFile.getId(), updatedFile.getId());
        assertEquals(cloudFile.getUser(), updatedFile.getUser());
        assertEquals(newFileName, updatedFile.getFileName());
        assertEquals(cloudFile.getStatus(), updatedFile.getStatus());
        assertNotEquals(cloudFile.getUpdatedAt(), updatedFile.getUpdatedAt());
        CloudFileUpdated updatedEvent = updatedEventCaptor.getValue();
        assertNotNull(updatedEvent);
        assertEquals(updatedFile, updatedEvent.getCloudFile());
        assertEquals(CloudFileTestDataFactory.getTraceId(), updatedEvent.getTraceId());
        assertNotNull(updatedEvent.getCreatedAt());
    }

    @Test
    void givenRequestAndNotUniqueName_whenUpdate_thenUniqueFileException() {
        given(dbRepository.uniqueNewName(cloudUser, newFileName)).willReturn(false);
        ArgumentCaptor<UpdateCloudFileError> errorEventCaptor = ArgumentCaptor.forClass(UpdateCloudFileError.class);

        Executable result = () -> sut.update(request);

        assertThrows(CloudFileException.class, result);
        verify(exceptionFactory, times(1))
                .create(CloudFileExceptionCode.NO_UNIQUE_FILE_NAME_ERROR, CloudFileTestDataFactory.getTraceId());
        verifyNoMoreInteractions(exceptionFactory);
        verify(dbRepository, times(1)).uniqueNewName(cloudUser, newFileName);
        verifyNoMoreInteractions(dbRepository);
        verify(eventPublisher, times(1)).publish(errorEventCaptor.capture());
        verifyNoMoreInteractions(eventPublisher);
        UpdateCloudFileError errorEvent = errorEventCaptor.getValue();
        assertNotNull(errorEvent);
        assertEquals(CloudFileExceptionCode.NO_UNIQUE_FILE_NAME_ERROR.code, errorEvent.getErrorMessage());
        assertEquals(CloudFileTestDataFactory.getTraceId(), errorEvent.getTraceId());
        assertNull(errorEvent.getCloudFile());
        assertNotNull(errorEvent.getCreatedAt());
    }

    @Test
    void givenRequestAndFileNotFound_whenUpdate_thenFileNotFoundException() {
        given(request.getFileName()).willReturn(CloudFileTestDataFactory.getTestFileName());
        given(dbRepository.uniqueNewName(cloudUser, newFileName)).willReturn(true);
        given(dbRepository.findByUserAndFileName(cloudUser, CloudFileTestDataFactory.getTestFileName()))
                .willReturn(Optional.empty());
        ArgumentCaptor<UpdateCloudFileError> errorEventCaptor = ArgumentCaptor.forClass(UpdateCloudFileError.class);

        Executable result = () -> sut.update(request);

        assertThrows(CloudFileException.class, result);
        verify(exceptionFactory, times(1))
                .create(CloudFileExceptionCode.FILE_NOT_FOUND_ERROR, CloudFileTestDataFactory.getTraceId());
        verifyNoMoreInteractions(exceptionFactory);
        verify(dbRepository, times(1)).uniqueNewName(cloudUser, newFileName);
        verify(dbRepository, times(1)).findByUserAndFileName(cloudUser, CloudFileTestDataFactory.getTestFileName());
        verifyNoMoreInteractions(dbRepository);
        verify(eventPublisher, times(1)).publish(errorEventCaptor.capture());
        verifyNoMoreInteractions(eventPublisher);
        UpdateCloudFileError errorEvent = errorEventCaptor.getValue();
        assertNotNull(errorEvent);
        assertEquals(CloudFileExceptionCode.FILE_NOT_FOUND_ERROR.code, errorEvent.getErrorMessage());
        assertEquals(CloudFileTestDataFactory.getTraceId(), errorEvent.getTraceId());
        assertNull(errorEvent.getCloudFile());
        assertNotNull(errorEvent.getCreatedAt());
    }

    @Test
    void givenRequestAndUpdateError_whenUpdate_thenDbUpdateException() {
        RuntimeException runtimeException = new RuntimeException("some-error-exception");
        given(request.getFileName()).willReturn(CloudFileTestDataFactory.getTestFileName());
        given(dbRepository.uniqueNewName(cloudUser, newFileName)).willReturn(true);
        given(dbRepository.findByUserAndFileName(cloudUser, CloudFileTestDataFactory.getTestFileName()))
                .willReturn(Optional.of(cloudFile));
        given(dbRepository.update(any(CoreCloudFile.class))).willThrow(runtimeException);
        ArgumentCaptor<UpdateCloudFileError> errorEventCaptor = ArgumentCaptor.forClass(UpdateCloudFileError.class);

        Executable result = () -> sut.update(request);

        assertThrows(CloudFileException.class, result);
        verify(exceptionFactory, times(1))
                .create(CloudFileExceptionCode.DB_UPDATE_ERROR, CloudFileTestDataFactory.getTraceId(),
                        runtimeException);
        verifyNoMoreInteractions(exceptionFactory);
        verify(dbRepository, times(1)).uniqueNewName(cloudUser, newFileName);
        verify(dbRepository, times(1)).findByUserAndFileName(cloudUser, CloudFileTestDataFactory.getTestFileName());
        verify(dbRepository, times(1)).update(any(CoreCloudFile.class));
        verifyNoMoreInteractions(dbRepository);
        verify(eventPublisher, times(1)).publish(errorEventCaptor.capture());
        verifyNoMoreInteractions(eventPublisher);
        UpdateCloudFileError errorEvent = errorEventCaptor.getValue();
        assertNotNull(errorEvent);
        assertEquals(runtimeException.getMessage(), errorEvent.getErrorMessage());
        assertEquals(CloudFileTestDataFactory.getTraceId(), errorEvent.getTraceId());
        assertEquals(cloudFile, errorEvent.getCloudFile());
        assertNotNull(errorEvent.getCreatedAt());
    }

    @Test
    void givenRequestAndPublisherError_whenUpdate_thenDbUpdateException() {
        CloudstorageEventPublisherException publisherException = new CloudstorageEventPublisherException(
                "some-publisher-exception");
        given(request.getFileName()).willReturn(CloudFileTestDataFactory.getTestFileName());
        given(dbRepository.uniqueNewName(cloudUser, newFileName)).willReturn(true);
        given(dbRepository.findByUserAndFileName(cloudUser, CloudFileTestDataFactory.getTestFileName()))
                .willReturn(Optional.of(cloudFile));
        given(eventPublisher.publish(any(CloudFileUpdated.class))).willThrow(publisherException);

        Executable result = () -> sut.update(request);

        assertThrows(CloudFileException.class, result);
        verify(exceptionFactory, times(1))
                .create(CloudFileExceptionCode.DB_UPDATE_ERROR, CloudFileTestDataFactory.getTraceId(),
                        publisherException);
        verifyNoMoreInteractions(exceptionFactory);
        verify(dbRepository, times(1)).uniqueNewName(cloudUser, newFileName);
        verify(dbRepository, times(1)).findByUserAndFileName(cloudUser, CloudFileTestDataFactory.getTestFileName());
        verify(dbRepository, times(1)).update(any(CoreCloudFile.class));
        verifyNoMoreInteractions(dbRepository);
        verify(eventPublisher, times(1)).publish(any(CloudFileUpdated.class));
        verifyNoMoreInteractions(eventPublisher);
    }
}
