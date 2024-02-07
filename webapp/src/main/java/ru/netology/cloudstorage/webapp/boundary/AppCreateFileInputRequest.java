package ru.netology.cloudstorage.webapp.boundary;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileInputRequest;
import ru.netology.cloudstorage.contracts.core.model.CloudUser;
import ru.netology.cloudstorage.contracts.core.model.TraceId;



@RequiredArgsConstructor
@Getter
public class AppCreateFileInputRequest implements CreateCloudFileInputRequest {

    @Valid
    private final AppCreateCloudFileResource userFile;

    @NotNull
    private final CloudUser user;

    @NotNull
    private final TraceId traceId;

    @Override
    public String toString() {
        return String.format(
                "AppCreateFileInputRequest{traceId=%s, filename=%s, size=%sb}",
                traceId.getId(),
                userFile.getFileName(),
                userFile.getSize()
        );
    }
}
