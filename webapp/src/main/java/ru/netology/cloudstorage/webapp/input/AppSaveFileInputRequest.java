package ru.netology.cloudstorage.webapp.input;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

@Value
public class AppSaveFileInputRequest {

    @NotEmpty
    private String filename;

    @NotNull
    private MultipartFile file;

    @Override
    public String toString() {
        return String.format(
                "AppSaveFileInputRequest{filename=%s,file=%s,size=%sb",
                filename,
                file.getOriginalFilename(),
                file.getSize()
        );
    }
}
