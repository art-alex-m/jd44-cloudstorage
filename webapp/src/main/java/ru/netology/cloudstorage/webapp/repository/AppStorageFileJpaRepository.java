package ru.netology.cloudstorage.webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netology.cloudstorage.webapp.entity.AppStorageFile;

import java.util.UUID;

@Repository
public interface AppStorageFileJpaRepository extends JpaRepository<AppStorageFile, UUID> {
}
