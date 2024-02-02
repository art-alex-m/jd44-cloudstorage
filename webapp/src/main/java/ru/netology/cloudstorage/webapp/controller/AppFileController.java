package ru.netology.cloudstorage.webapp.controller;


import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.cloudstorage.contracts.auth.model.PermissionFiles;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileInput;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileInputRequest;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileInputResponse;
import ru.netology.cloudstorage.contracts.core.model.CloudUser;
import ru.netology.cloudstorage.contracts.core.model.TraceId;
import ru.netology.cloudstorage.webapp.boundary.AppCreateCloudFileResource;
import ru.netology.cloudstorage.webapp.boundary.AppCreateFileInputRequest;

/**
 * AppFileController
 *
 * <p>
 * <a href="https://www.baeldung.com/spring-bean-scopes#1-request-scope">Quick Guide to Spring Bean Scopes</a><br>
 * <a href="https://www.baeldung.com/spring-inject-prototype-bean-into-singleton">Injecting Prototype Beans into a Singleton Instance in Spring</a><br>
 * <a href="https://www.baeldung.com/get-user-in-spring-security#interface">Get the User via a Custom Interface</a><br>
 * </p>
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class AppFileController {

    @Resource
    private TraceId requestTraceId;

    private final CreateCloudFileInput createCloudFileInteractor;

    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Secured(PermissionFiles.CREATE)
    public CreateCloudFileInputResponse createCloudFile(@Validated AppCreateCloudFileResource userFile,
            @AuthenticationPrincipal CloudUser user) {
        CreateCloudFileInputRequest request = new AppCreateFileInputRequest(userFile, user, requestTraceId);
        return createCloudFileInteractor.create(request);
    }

    @GetMapping("/list")
    @Secured(PermissionFiles.LIST)
    public String getFilesList(int limit, @AuthenticationPrincipal CloudUser user) {
        log.info("traceId={}, hashCode={}; {}", requestTraceId.getId(), user.getId().hashCode(), user);
        return "No";
    }
}
