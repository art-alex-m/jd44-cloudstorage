package ru.netology.cloudstorage.core.boundary.list;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.netology.cloudstorage.contracts.core.boundary.list.ListCloudFileInputRequest;
import ru.netology.cloudstorage.contracts.core.boundary.list.ListCloudFileInputResponse;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.CloudUser;
import ru.netology.cloudstorage.contracts.db.repository.ListCloudFileInputDbRepository;
import ru.netology.cloudstorage.core.factory.CloudFileTestDataFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CoreListCloudFileInteractorTest {

    static CloudFileTestDataFactory dataFactory = new CloudFileTestDataFactory();

    @Mock
    ListCloudFileInputDbRepository dbRepository;

    @Mock
    CloudUser cloudUser;

    @Mock
    CloudFile cloudFile;

    @InjectMocks
    CoreListCloudFileInteractor sut;

    @Test
    void givenRequest_whenFind_thenSuccess() {
        given(dbRepository.findByUserAndReadyStatus(cloudUser, 15)).willReturn(List.of(cloudFile));
        ListCloudFileInputRequest request = new CoreListCloudFileInputRequest(cloudUser, 15, dataFactory.getTraceId());

        ListCloudFileInputResponse result = sut.find(request);

        assertNotNull(result);
        assertEquals(1, result.getCloudFiles().size());
        assertEquals(cloudFile, result.getCloudFiles().get(0));
        verify(dbRepository, times(1)).findByUserAndReadyStatus(cloudUser, 15);
    }
}