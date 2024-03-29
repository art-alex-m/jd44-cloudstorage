package ru.netology.cloudstorage.webapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.netology.cloudstorage.contracts.core.boundary.CloudFileErrorStatusAction;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileInput;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileReadyAction;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileStorageDbSaveAction;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileStorageUploadAction;
import ru.netology.cloudstorage.contracts.core.boundary.delete.DeleteCloudFileAction;
import ru.netology.cloudstorage.contracts.core.boundary.delete.DeleteCloudFileInput;
import ru.netology.cloudstorage.contracts.core.boundary.delete.DeleteCloudFileStorageDeleteAction;
import ru.netology.cloudstorage.contracts.core.boundary.download.DownloadCloudFileInput;
import ru.netology.cloudstorage.contracts.core.boundary.list.ListCloudFileInput;
import ru.netology.cloudstorage.contracts.core.boundary.update.UpdateCloudFileInput;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileExceptionFactory;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileStatusFactory;
import ru.netology.cloudstorage.contracts.core.factory.CreateCloudFileInputResponseFactory;
import ru.netology.cloudstorage.contracts.db.repository.CloudFileErrorStatusDbRepository;
import ru.netology.cloudstorage.contracts.db.repository.CreateCloudFileInputDbRepository;
import ru.netology.cloudstorage.contracts.db.repository.DeleteCloudFileActionDbRepository;
import ru.netology.cloudstorage.contracts.db.repository.DeleteCloudFileInputDbRepository;
import ru.netology.cloudstorage.contracts.db.repository.ListCloudFileInputDbRepository;
import ru.netology.cloudstorage.contracts.event.handler.CloudstorageEventPublisher;
import ru.netology.cloudstorage.contracts.storage.repository.CreateCloudFileStorageUploadRepository;
import ru.netology.cloudstorage.contracts.storage.repository.DeleteCloudFileStorageRepository;
import ru.netology.cloudstorage.contracts.storage.repository.DownloadCloudFileStorageRepository;
import ru.netology.cloudstorage.core.boundary.CoreCloudFileErrorStatusAction;
import ru.netology.cloudstorage.core.boundary.create.CoreCreateCloudFileInteractor;
import ru.netology.cloudstorage.core.boundary.create.CoreCreateCloudFileReadyAction;
import ru.netology.cloudstorage.core.boundary.create.CoreCreateCloudFileStorageDbSaveAction;
import ru.netology.cloudstorage.core.boundary.create.CoreCreateCloudFileStorageUploadAction;
import ru.netology.cloudstorage.core.boundary.delete.CoreDeleteCloudFileAction;
import ru.netology.cloudstorage.core.boundary.delete.CoreDeleteCloudFileInteractor;
import ru.netology.cloudstorage.core.boundary.delete.CoreDeleteCloudFileStorageDeleteAction;
import ru.netology.cloudstorage.core.boundary.download.CoreDownloadCloudFileInteractor;
import ru.netology.cloudstorage.core.boundary.list.CoreListCloudFileInteractor;
import ru.netology.cloudstorage.core.boundary.update.CoreUpdateCloudFileInteractor;
import ru.netology.cloudstorage.core.factory.CoreCloudFileExceptionFactory;
import ru.netology.cloudstorage.core.factory.CoreCloudFileStatusFactory;
import ru.netology.cloudstorage.storage.local.repository.LocalStorageFileRepository;
import ru.netology.cloudstorage.webapp.repository.AppDownloadCloudFileInputDbRepository;
import ru.netology.cloudstorage.webapp.repository.AppUpdateCloudFileInputDbRepository;

import java.nio.file.Path;

/**
 * Конфигуратор сценариев приложения
 */
@Configuration
public class AppInteractorsConfiguration {

    /// Сценарий загрузки файла "Сохранение файла"

    @Bean
    public CreateCloudFileInput coreCreateCloudFileInput(CloudFileStatusFactory coreCloudFileStatusFactory,
            CreateCloudFileInputResponseFactory appCreateCloudFileInputResponseFactory,
            CreateCloudFileInputDbRepository appCreateCloudFileInputDbRepository,
            CloudstorageEventPublisher appCloudstorageEventPublisher,
            CloudFileExceptionFactory coreCloudFileExceptionFactory) {

        return CoreCreateCloudFileInteractor.builder()
                .statusFactory(coreCloudFileStatusFactory)
                .responseFactory(appCreateCloudFileInputResponseFactory)
                .dbRepository(appCreateCloudFileInputDbRepository)
                .eventPublisher(appCloudstorageEventPublisher)
                .exceptionFactory(coreCloudFileExceptionFactory)
                .build();
    }

    @Bean
    public CreateCloudFileStorageUploadAction coreCreateCloudFileStorageUploadAction(
            CloudFileStatusFactory coreCloudFileStatusFactory,
            CreateCloudFileStorageUploadRepository createCloudFileStorageUploadRepository,
            CloudstorageEventPublisher appCloudstorageEventPublisher,
            CloudFileExceptionFactory coreCloudFileExceptionFactory) {

        return CoreCreateCloudFileStorageUploadAction.builder()
                .uploadRepository(createCloudFileStorageUploadRepository)
                .statusFactory(coreCloudFileStatusFactory)
                .exceptionFactory(coreCloudFileExceptionFactory)
                .eventPublisher(appCloudstorageEventPublisher)
                .build();
    }

    @Bean
    public CreateCloudFileStorageDbSaveAction coreCreateCloudFileStorageDbSaveAction(
            CloudstorageEventPublisher appCloudstorageEventPublisher,
            CloudFileExceptionFactory coreCloudFileExceptionFactory,
            CreateCloudFileInputDbRepository appCreateCloudFileInputDbRepository) {

        return CoreCreateCloudFileStorageDbSaveAction.builder()
                .dbRepository(appCreateCloudFileInputDbRepository)
                .exceptionFactory(coreCloudFileExceptionFactory)
                .eventPublisher(appCloudstorageEventPublisher)
                .build();
    }

    @Bean
    public CreateCloudFileReadyAction coreCreateCloudFileReadyAction(
            CloudFileStatusFactory coreCloudFileStatusFactory,
            CreateCloudFileInputDbRepository appCreateCloudFileInputDbRepository,
            CloudstorageEventPublisher appCloudstorageEventPublisher,
            CloudFileExceptionFactory coreCloudFileExceptionFactory) {

        return CoreCreateCloudFileReadyAction.builder()
                .statusFactory(coreCloudFileStatusFactory)
                .dbRepository(appCreateCloudFileInputDbRepository)
                .eventPublisher(appCloudstorageEventPublisher)
                .exceptionFactory(coreCloudFileExceptionFactory)
                .build();
    }

    @Bean
    public CloudFileErrorStatusAction coreCloudFileErrorStatusAction(
            CloudFileErrorStatusDbRepository appCloudFileErrorStatusDbRepository,
            CloudFileStatusFactory coreCloudFileStatusFactory,
            CloudFileExceptionFactory coreCloudFileExceptionFactory) {

        return CoreCloudFileErrorStatusAction.builder()
                .dbRepository(appCloudFileErrorStatusDbRepository)
                .exceptionFactory(coreCloudFileExceptionFactory)
                .statusFactory(coreCloudFileStatusFactory)
                .build();
    }


    /// Сценарий просмотра списка файлов пользователя

    @Bean
    public ListCloudFileInput coreListCloudFileInput(ListCloudFileInputDbRepository appListCloudFileInputDbRepository) {
        return new CoreListCloudFileInteractor(appListCloudFileInputDbRepository);
    }

    /// Сценарий обновления имени файла

    @Bean
    public UpdateCloudFileInput coreUpdateCloudFileInput(
            AppUpdateCloudFileInputDbRepository appUpdateCloudFileInputDbRepository,
            CloudstorageEventPublisher appCloudstorageEventPublisher,
            CloudFileExceptionFactory coreCloudFileExceptionFactory) {

        return CoreUpdateCloudFileInteractor.builder()
                .dbRepository(appUpdateCloudFileInputDbRepository)
                .exceptionFactory(coreCloudFileExceptionFactory)
                .eventPublisher(appCloudstorageEventPublisher)
                .build();
    }

    /// Сценарий скачивания файла
    @Bean
    public DownloadCloudFileInput coreDownloadCloudFileInput(
            AppDownloadCloudFileInputDbRepository appDownloadCloudFileInputDbRepository,
            DownloadCloudFileStorageRepository downloadCloudFileStorageRepository,
            CloudFileExceptionFactory coreCloudFileExceptionFactory) {

        return CoreDownloadCloudFileInteractor.builder()
                .dbRepository(appDownloadCloudFileInputDbRepository)
                .storageRepository(downloadCloudFileStorageRepository)
                .exceptionFactory(coreCloudFileExceptionFactory)
                .build();
    }

    /// Сценарий удаления файла
    @Bean
    public DeleteCloudFileInput coreDeleteCloudFileInput(CloudstorageEventPublisher appCloudstorageEventPublisher,
            CloudFileExceptionFactory coreCloudFileExceptionFactory, CloudFileStatusFactory coreCloudFileStatusFactory,
            DeleteCloudFileInputDbRepository appCreateCloudFileInputDbRepository) {

        return CoreDeleteCloudFileInteractor.builder()
                .dbRepository(appCreateCloudFileInputDbRepository)
                .statusFactory(coreCloudFileStatusFactory)
                .eventPublisher(appCloudstorageEventPublisher)
                .exceptionFactory(coreCloudFileExceptionFactory)
                .build();
    }

    @Bean
    public DeleteCloudFileStorageDeleteAction coreDeleteCloudFileStorageDeleteAction(
            CloudstorageEventPublisher appCloudstorageEventPublisher,
            CloudFileExceptionFactory coreCloudFileExceptionFactory,
            DeleteCloudFileStorageRepository localStorageFileRepository) {

        return CoreDeleteCloudFileStorageDeleteAction.builder()
                .eventPublisher(appCloudstorageEventPublisher)
                .exceptionFactory(coreCloudFileExceptionFactory)
                .storageRepository(localStorageFileRepository)
                .build();
    }

    @Bean
    public DeleteCloudFileAction coreDeleteCloudFileAction(CloudstorageEventPublisher appCloudstorageEventPublisher,
            CloudFileExceptionFactory coreCloudFileExceptionFactory,
            DeleteCloudFileActionDbRepository appDeleteCloudFileActionDbRepository) {

        return CoreDeleteCloudFileAction.builder()
                .dbRepository(appDeleteCloudFileActionDbRepository)
                .eventPublisher(appCloudstorageEventPublisher)
                .exceptionFactory(coreCloudFileExceptionFactory)
                .build();
    }

    /// Базовые компоненты сценариев

    @Bean
    public CloudFileStatusFactory coreCloudFileStatusFactory() {
        return new CoreCloudFileStatusFactory();
    }

    @Bean
    public CloudFileExceptionFactory coreCloudFileExceptionFactory() {
        return new CoreCloudFileExceptionFactory();
    }

    @Bean
    public LocalStorageFileRepository localStorageFileRepository(
            @Value("${cloudstorage.storage.local.base-path}") String basePath) {
        return new LocalStorageFileRepository(Path.of(basePath));
    }
}
