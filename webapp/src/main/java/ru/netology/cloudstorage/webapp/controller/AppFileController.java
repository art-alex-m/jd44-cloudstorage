package ru.netology.cloudstorage.webapp.controller;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.cloudstorage.contracts.auth.model.PermissionFiles;
import ru.netology.cloudstorage.contracts.core.boundary.CloudFileInputResponse;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileInput;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileInputRequest;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileInputResponse;
import ru.netology.cloudstorage.contracts.core.boundary.delete.DeleteCloudFileInput;
import ru.netology.cloudstorage.contracts.core.boundary.delete.DeleteCloudFileInputRequest;
import ru.netology.cloudstorage.contracts.core.boundary.delete.DeleteCloudFileInputResponse;
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
import ru.netology.cloudstorage.contracts.trace.model.TraceIdContainer;
import ru.netology.cloudstorage.core.boundary.delete.CoreDeleteCloudFileInputRequest;
import ru.netology.cloudstorage.core.boundary.download.CoreDownloadCloudFileInputRequest;
import ru.netology.cloudstorage.core.boundary.list.CoreListCloudFileInputRequest;
import ru.netology.cloudstorage.core.boundary.update.CoreUpdateCloudFileInputRequest;
import ru.netology.cloudstorage.webapp.boundary.AppCloudFileInputResponse;
import ru.netology.cloudstorage.webapp.boundary.AppCreateCloudFileResource;
import ru.netology.cloudstorage.webapp.boundary.AppCreateFileInputRequest;
import ru.netology.cloudstorage.webapp.boundary.AppListCloudFileInputResponse;
import ru.netology.cloudstorage.webapp.boundary.AppUpdateCloudFileInputRequest;
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
@AllArgsConstructor
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class AppFileController {

    private final CreateCloudFileInput createCloudFileInteractor;

    private final ListCloudFileInput listCloudFileInteractor;
    private final AppListCloudFileInputResponsePresenter listCloudFileInputResponsePresenter;

    private final UpdateCloudFileInput updateCloudFileInteractor;

    private final DownloadCloudFileInput downloadCloudFileInteractor;

    private final DeleteCloudFileInput deleteCloudFileInteractor;

    @Setter
    private TraceIdContainer traceIdContainer;

    /**
     * Создание файла
     */
    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Secured(PermissionFiles.CREATE)
    public CreateCloudFileInputResponse createCloudFile(@Validated AppCreateCloudFileResource userFile,
            @AuthenticationPrincipal CloudUser user) {
        TraceId requestTraceId = traceIdContainer.getTraceId();
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
        TraceId requestTraceId = traceIdContainer.getTraceId();
        ListCloudFileInputRequest request = new CoreListCloudFileInputRequest(user, limit, requestTraceId);
        ListCloudFileInputResponse response = listCloudFileInteractor.find(request);
        return listCloudFileInputResponsePresenter.format(response);
    }

    /**
     * Обновление файла пользователя
     */
    @PutMapping("/file")
    @Secured(PermissionFiles.UPDATE)
    public CloudFileInputResponse updateFile(@RequestParam(name = "filename") String fileName,
            @Validated @RequestBody AppUpdateCloudFileInputRequest apiRequest,
            @AuthenticationPrincipal CloudUser user) {

        TraceId requestTraceId = traceIdContainer.getTraceId();
        UpdateCloudFileInputRequest request = CoreUpdateCloudFileInputRequest.builder()
                .fileName(fileName)
                .newFileName(apiRequest.getNewFileName())
                .traceId(requestTraceId)
                .user(user).build();

        UpdateCloudFileInputResponse response = updateCloudFileInteractor.update(request);

        return new AppCloudFileInputResponse(response);
    }

    /**
     * Загрузка файла
     */
    @GetMapping("/file")
    @Secured(PermissionFiles.DOWNLOAD)
    public ResponseEntity<InputStreamResource> downloadFile(
            @Validated @RequestParam(name = "filename") @NotEmpty String fileName,
            @AuthenticationPrincipal CloudUser user) throws IOException {

        TraceId requestTraceId = traceIdContainer.getTraceId();
        DownloadCloudFileInputRequest request = new CoreDownloadCloudFileInputRequest(fileName, requestTraceId, user);
        FileResource fileResource = downloadCloudFileInteractor.getResource(request);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(fileResource.getMediaType()));
        headers.setContentLength(fileResource.getSize());
        headers.setContentDispositionFormData("attachment", fileName);

        return new ResponseEntity<>(new InputStreamResource(fileResource.getInputStream()), headers, HttpStatus.OK);
    }

    /**
     * Удаление файла пользователя
     */
    @DeleteMapping("/file")
    @Secured(PermissionFiles.DELETE)
    public CloudFileInputResponse deleteFile(
            @Validated @RequestParam(name = "filename") @NotEmpty String fileName,
            @AuthenticationPrincipal CloudUser user) {

        TraceId requestTraceId = traceIdContainer.getTraceId();
        DeleteCloudFileInputRequest request = new CoreDeleteCloudFileInputRequest(fileName, requestTraceId, user);
        DeleteCloudFileInputResponse response = deleteCloudFileInteractor.delete(request);

        return new AppCloudFileInputResponse(response);
    }
}
