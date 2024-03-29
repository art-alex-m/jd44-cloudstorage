package ru.netology.cloudstorage.webapp.boundary;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudstorage.contracts.core.model.FileResource;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;

@Getter
@RequiredArgsConstructor
public class AppCreateCloudFileResource implements FileResource {

    private final Instant createdAt = Instant.now();

    private final Instant updatedAt = Instant.now();

    @NotEmpty
    private final String filename;

    @NotNull
    @JsonIgnore
    private final MultipartFile file;

    @Override
    @JsonIgnore
    public long getSize() {
        return file.getSize();
    }

    @Override
    public String getFileName() {
        return filename;
    }

    @Override
    @JsonIgnore
    public String getMediaType() {
        return file.getContentType();
    }

    @Override
    @JsonIgnore
    public InputStream getInputStream() throws IOException {
        return file.getInputStream();
    }
}
