package ru.netology.cloudstorage.webapp.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.cloudstorage.contracts.auth.model.PermissionFiles;
import ru.netology.cloudstorage.webapp.entity.AppUser;
import ru.netology.cloudstorage.webapp.input.AppSaveFileInputRequest;

@Slf4j
@RestController
public class AppFileController {

    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Secured(PermissionFiles.CREATE)
    public String createCloudFile(@Validated AppSaveFileInputRequest request) {
        log.info(request.toString());
        return "Ok";
    }

    @GetMapping("/list")
    @Secured(PermissionFiles.LIST)
    public String getFilesList(int limit, @AuthenticationPrincipal AppUser user) {
        log.info("hashCode={}; {}", user.getId().hashCode(), user);
        return "No";
    }
}
