package ru.netology.cloudstorage.webapp.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatusCode;
import ru.netology.cloudstorage.webapp.entity.AppCloudFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppCloudFileJpaRepository extends JpaRepository<AppCloudFile, UUID> {

    @Query("select cfile from AppCloudFile cfile where cfile.user.id = :userId " +
            "and cfile.statusLatest.status.code = 'READY' " +
            "order by cfile.createdAt desc, cfile.fileName")
    List<CloudFile> findByUserIdAndReadyStatusAndLimit(@Param("userId") UUID userId, Pageable page);

    boolean existsByUserIdAndFileName(UUID userId, String fileName);

    Optional<CloudFile> findByUserIdAndFileName(UUID userId, String fileName);

    Optional<CloudFile> findByUserIdAndFileNameAndStatusLatest_Status_Code(UUID userId, String fileName,
            CloudFileStatusCode code);
}
