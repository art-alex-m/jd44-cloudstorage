package ru.netology.cloudstorage.core.boundary.download;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.netology.cloudstorage.contracts.core.boundary.download.DownloadCloudFileInputRequest;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileException;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileExceptionCode;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileExceptionFactory;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.CloudUser;
import ru.netology.cloudstorage.contracts.core.model.FileResource;
import ru.netology.cloudstorage.contracts.core.model.StorageFile;
import ru.netology.cloudstorage.contracts.core.model.TraceId;
import ru.netology.cloudstorage.contracts.db.repository.DownloadCloudFileInputDbRepository;
import ru.netology.cloudstorage.contracts.storage.repository.DownloadCloudFileStorageRepository;
import ru.netology.cloudstorage.core.factory.CoreCloudFileExceptionFactory;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.verifyNoInteractions;
import static org.mockito.BDDMockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class CoreDownloadCloudFileInteractorTest {

    String testFileName = "test-file-name";

    @Mock
    DownloadCloudFileInputRequest request;

    @Mock
    FileResource fileResource;

    @Mock
    CloudFile cloudFile;

    @Mock
    StorageFile storageFile;

    @Mock
    TraceId traceId;

    @Mock
    CloudUser cloudUser;

    @Mock
    DownloadCloudFileInputDbRepository dbRepository;

    @Mock
    DownloadCloudFileStorageRepository storageRepository;

    @Spy
    CloudFileExceptionFactory exceptionFactory = new CoreCloudFileExceptionFactory();

    @InjectMocks
    CoreDownloadCloudFileInteractor sut;

    @BeforeEach
    void setUp() {
        given(request.getUser()).willReturn(cloudUser);
        given(request.getFileName()).willReturn(testFileName);
    }

    @Test
    void givenRequest_whenGetResource_thenSuccess() {
        given(cloudFile.getStorageFile()).willReturn(storageFile);
        given(dbRepository.findByUserAndFileNameAndReadyStatus(cloudUser, testFileName))
                .willReturn(Optional.of(cloudFile));
        given(storageRepository.getResource(storageFile)).willReturn(fileResource);

        FileResource result = sut.getResource(request);

        assertNotNull(result);
        assertEquals(fileResource, result);
        verify(dbRepository, times(1)).findByUserAndFileNameAndReadyStatus(cloudUser, testFileName);
        verify(storageRepository, times(1)).getResource(storageFile);
        verifyNoMoreInteractions(dbRepository);
        verifyNoMoreInteractions(storageFile);
    }

    @Test
    void givenEmptyDb_whenGetResource_thenFileNotFoundException() {
        given(request.getTraceId()).willReturn(traceId);
        given(dbRepository.findByUserAndFileNameAndReadyStatus(cloudUser, testFileName)).willReturn(Optional.empty());

        Executable result = () -> sut.getResource(request);

        assertThrows(CloudFileException.class, result);
        verify(dbRepository, times(1)).findByUserAndFileNameAndReadyStatus(cloudUser, testFileName);
        verify(exceptionFactory, times(1)).create(CloudFileExceptionCode.FILE_NOT_FOUND_ERROR, traceId);
        verifyNoMoreInteractions(dbRepository);
        verifyNoMoreInteractions(exceptionFactory);
        verifyNoInteractions(storageFile);
    }
}
