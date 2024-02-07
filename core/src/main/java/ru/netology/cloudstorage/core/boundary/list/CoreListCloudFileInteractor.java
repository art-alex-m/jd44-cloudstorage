package ru.netology.cloudstorage.core.boundary.list;

import lombok.RequiredArgsConstructor;
import ru.netology.cloudstorage.contracts.core.boundary.list.ListCloudFileInput;
import ru.netology.cloudstorage.contracts.core.boundary.list.ListCloudFileInputRequest;
import ru.netology.cloudstorage.contracts.core.boundary.list.ListCloudFileInputResponse;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.db.repository.ListCloudFileInputDbRepository;

import java.util.List;

@RequiredArgsConstructor
public class CoreListCloudFileInteractor implements ListCloudFileInput {

    private final ListCloudFileInputDbRepository dbRepository;

    @Override
    public ListCloudFileInputResponse find(ListCloudFileInputRequest request) {
        List<CloudFile> cloudFileList = dbRepository.findByUserAndReadyStatus(request.getUser(), request.getLimit());
        return new CoreListCloudFileInputResponse(cloudFileList);
    }
}
