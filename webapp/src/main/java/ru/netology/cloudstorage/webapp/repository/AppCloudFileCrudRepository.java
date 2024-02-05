package ru.netology.cloudstorage.webapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.netology.cloudstorage.webapp.entity.AppCloudFile;

import java.util.UUID;

@Repository
public interface AppCloudFileCrudRepository extends CrudRepository<AppCloudFile, UUID> {
}
