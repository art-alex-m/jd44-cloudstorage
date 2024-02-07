package ru.netology.cloudstorage.webapp.factory;

import org.springframework.stereotype.Component;
import ru.netology.cloudstorage.contracts.core.boundary.list.ListCloudFileInputResponse;
import ru.netology.cloudstorage.webapp.boundary.AppListCloudFileInputResponse;

import java.util.List;

@Component
public class AppListCloudFileInputResponsePresenter {
    public List<AppListCloudFileInputResponse> format(ListCloudFileInputResponse response) {
        return response.getCloudFiles().stream()
                .map(file -> new AppListCloudFileInputResponse(file.getFileName(),
                        file.getStorageFile().getSize(), file.getUpdatedAt()))
                .toList();
    }
}
