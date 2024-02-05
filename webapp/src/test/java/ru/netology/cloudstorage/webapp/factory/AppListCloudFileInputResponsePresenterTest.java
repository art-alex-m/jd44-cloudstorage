package ru.netology.cloudstorage.webapp.factory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.netology.cloudstorage.contracts.core.boundary.list.ListCloudFileInputResponse;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.StorageFile;
import ru.netology.cloudstorage.core.boundary.list.CoreListCloudFileInputResponse;
import ru.netology.cloudstorage.webapp.boundary.AppListCloudFileInputResponse;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AppListCloudFileInputResponsePresenterTest {

    @Mock
    CloudFile cloudFile;

    @Mock
    StorageFile storageFile;

    @Test
    void givenListResponse_whenFormat_thenSuccess() {
        long testSize = 150L;
        String testFileName = "test-file-name.jpg";
        given(storageFile.getSize()).willReturn(testSize);
        given(cloudFile.getFileName()).willReturn(testFileName);
        given(cloudFile.getStorageFile()).willReturn(storageFile);
        given(cloudFile.getUpdatedAt()).willReturn(Instant.now());
        ListCloudFileInputResponse response = new CoreListCloudFileInputResponse(List.of(cloudFile));
        AppListCloudFileInputResponsePresenter sut = new AppListCloudFileInputResponsePresenter();

        List<AppListCloudFileInputResponse> result = sut.format(response);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testSize, result.get(0).getSize());
        assertEquals(testFileName, result.get(0).getFileName());
        assertEquals(cloudFile.getUpdatedAt(), result.get(0).getUpdatedAt());
    }

    @Test
    void givenEmptyResponse_whenFormat_thenSuccess() {
        ListCloudFileInputResponse response = new CoreListCloudFileInputResponse(Collections.emptyList());
        AppListCloudFileInputResponsePresenter sut = new AppListCloudFileInputResponsePresenter();

        List<AppListCloudFileInputResponse> result = sut.format(response);

        assertNotNull(result);
        assertEquals(0, result.size());
    }
}
