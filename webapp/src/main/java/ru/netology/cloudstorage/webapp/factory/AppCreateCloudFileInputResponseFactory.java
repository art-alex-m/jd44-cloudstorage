package ru.netology.cloudstorage.webapp.factory;

import org.springframework.stereotype.Component;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileInputResponse;
import ru.netology.cloudstorage.contracts.core.factory.CreateCloudFileInputResponseFactory;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.webapp.boundary.AppCreateCloudFileInputResponse;

@Component("appCreateCloudFileInputResponseFactory")
public class AppCreateCloudFileInputResponseFactory implements CreateCloudFileInputResponseFactory {
    @Override
    public CreateCloudFileInputResponse create(CloudFile cloudFile) {
        return new AppCreateCloudFileInputResponse(cloudFile.getId(), cloudFile.getStatus());
    }
}
