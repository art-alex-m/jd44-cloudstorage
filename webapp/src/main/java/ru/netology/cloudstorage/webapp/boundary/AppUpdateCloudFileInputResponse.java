package ru.netology.cloudstorage.webapp.boundary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.netology.cloudstorage.contracts.core.boundary.update.UpdateCloudFileInputResponse;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatus;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatusCode;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class AppUpdateCloudFileInputResponse {
    @JsonProperty("id")
    private final UUID cloudFileId;

    @JsonIgnore
    private final CloudFileStatus cloudFileStatus;

    public AppUpdateCloudFileInputResponse(UpdateCloudFileInputResponse response) {
        this.cloudFileId = response.getCloudFileId();
        this.cloudFileStatus = response.getCloudFileStatus();
    }

    @JsonProperty("status")
    public CloudFileStatusCode getStatusCode() {
        return cloudFileStatus.getCode();
    }
}
