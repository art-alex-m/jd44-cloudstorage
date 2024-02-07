package ru.netology.cloudstorage.webapp.boundary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileInputResponse;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatus;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatusCode;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class AppCreateCloudFileInputResponse implements CreateCloudFileInputResponse {
    @JsonProperty("id")
    private final UUID cloudFileId;

    @JsonIgnore
    private final CloudFileStatus cloudFileStatus;

    @JsonProperty("status")
    public CloudFileStatusCode getStatusCode() {
        return cloudFileStatus.getCode();
    }
}
