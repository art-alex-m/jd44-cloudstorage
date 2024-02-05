package ru.netology.cloudstorage.core.boundary.list;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.netology.cloudstorage.contracts.core.boundary.list.ListCloudFileInputResponse;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class CoreListCloudFileInputResponse implements ListCloudFileInputResponse {
    private final List<CloudFile> cloudFiles;
}
