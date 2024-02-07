package ru.netology.cloudstorage.webapp.controller;


import jakarta.annotation.Resource;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.netology.cloudstorage.contracts.auth.model.PermissionFiles;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileInput;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileInputRequest;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileInputResponse;
import ru.netology.cloudstorage.contracts.core.boundary.download.DownloadCloudFileInput;
import ru.netology.cloudstorage.contracts.core.boundary.download.DownloadCloudFileInputRequest;
import ru.netology.cloudstorage.contracts.core.boundary.list.ListCloudFileInput;
import ru.netology.cloudstorage.contracts.core.boundary.list.ListCloudFileInputRequest;
import ru.netology.cloudstorage.contracts.core.boundary.list.ListCloudFileInputResponse;
import ru.netology.cloudstorage.contracts.core.boundary.update.UpdateCloudFileInput;
import ru.netology.cloudstorage.contracts.core.boundary.update.UpdateCloudFileInputRequest;
import ru.netology.cloudstorage.contracts.core.boundary.update.UpdateCloudFileInputResponse;
import ru.netology.cloudstorage.contracts.core.model.CloudUser;
import ru.netology.cloudstorage.contracts.core.model.FileResource;
import ru.netology.cloudstorage.contracts.core.model.TraceId;
import ru.netology.cloudstorage.core.boundary.download.CoreDownloadCloudFileInputRequest;
import ru.netology.cloudstorage.core.boundary.list.CoreListCloudFileInputRequest;
import ru.netology.cloudstorage.core.boundary.update.CoreUpdateCloudFileInputRequest;
import ru.netology.cloudstorage.webapp.boundary.*;
import ru.netology.cloudstorage.webapp.factory.AppListCloudFileInputResponsePresenter;

import java.io.IOException;
import java.util.List;

/**
 * AppFileController
 *
 * <p>
 * <a href="https://www.baeldung.com/spring-bean-scopes#1-request-scope">Quick Guide to Spring Bean Scopes</a><br>
 * <a href="https://www.baeldung.com/spring-inject-prototype-bean-into-singleton">Injecting Prototype Beans into a Singleton Instance in Spring</a><br>
 * <a href="https://www.baeldung.com/get-user-in-spring-security#interface">Get the User via a Custom Interface</a><br>
 * </p>
 */
@RestController
@RequiredArgsConstructor
public class AppFileController {

    @Resource
    private TraceId requestTraceId;

    private final CreateCloudFileInput createCloudFileInteractor;

    private final ListCloudFileInput listCloudFileInteractor;
    private final AppListCloudFileInputResponsePresenter listCloudFileInputResponsePresenter;

    private final UpdateCloudFileInput updateCloudFileInteractor;

    private final DownloadCloudFileInput downloadCloudFileInteractor;

    /**
     * Создание файла
     */
    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Secured(PermissionFiles.CREATE)
    public CreateCloudFileInputResponse createCloudFile(@Validated AppCreateCloudFileResource userFile,
            @AuthenticationPrincipal CloudUser user) {
        CreateCloudFileInputRequest request = new AppCreateFileInputRequest(userFile, user, requestTraceId);
        return createCloudFileInteractor.create(request);
    }

    /**
     * Получение списка файлов пользователя
     */
    @GetMapping("/list")
    @Secured(PermissionFiles.LIST)
    public List<AppListCloudFileInputResponse> getFilesList(@Validated @Positive int limit,
            @AuthenticationPrincipal CloudUser user) {
        ListCloudFileInputRequest request = new CoreListCloudFileInputRequest(user, limit, requestTraceId);
        ListCloudFileInputResponse response = listCloudFileInteractor.find(request);
        return listCloudFileInputResponsePresenter.format(response);
    }

    /**
     * Обновление файла пользователя
     */
    @PutMapping("/file")
    @Secured(PermissionFiles.UPDATE)
    public AppUpdateCloudFileInputResponse updateFile(@RequestParam(name = "filename") String fileName,
            @Validated @RequestBody AppUpdateCloudFileInputRequest apiRequest,
            @AuthenticationPrincipal CloudUser user) {

        UpdateCloudFileInputRequest request = CoreUpdateCloudFileInputRequest.builder()
                .fileName(fileName)
                .newFileName(apiRequest.getNewFileName())
                .traceId(requestTraceId)
                .user(user).build();

        UpdateCloudFileInputResponse response = updateCloudFileInteractor.update(request);

        return new AppUpdateCloudFileInputResponse(response);
    }

    /**
     * Загрузка файла
     */
    @GetMapping("/file")
    @Secured(PermissionFiles.DOWNLOAD)
    public ResponseEntity<InputStreamResource> downloadFile(@RequestParam(name = "filename") String fileName,
            @AuthenticationPrincipal CloudUser user) throws IOException {

        DownloadCloudFileInputRequest request = new CoreDownloadCloudFileInputRequest(fileName, requestTraceId, user);
        FileResource fileResource = downloadCloudFileInteractor.getResource(request);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(fileResource.getMediaType()));
        headers.setContentLength(fileResource.getSize());
        headers.setContentDispositionFormData("attachment", fileName);

        return new ResponseEntity<>(new InputStreamResource(fileResource.getInputStream()), headers, HttpStatus.OK);
    }
}
