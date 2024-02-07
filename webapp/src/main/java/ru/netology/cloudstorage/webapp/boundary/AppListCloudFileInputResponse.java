package ru.netology.cloudstorage.webapp.boundary;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@RequiredArgsConstructor
@Getter
public class AppListCloudFileInputResponse implements Serializable {

    @JsonProperty("filename")
    private final String fileName;

    private final long size;

    @JsonProperty("editedAt")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    private final Instant updatedAt;
}
