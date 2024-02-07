package ru.netology.cloudstorage.webapp.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatus;
import ru.netology.cloudstorage.contracts.core.model.CloudUser;
import ru.netology.cloudstorage.contracts.core.model.StorageFile;

import java.time.Instant;
import java.util.UUID;

/**
 * ORM представление CloudFile
 *
 * <p>
 * <a href="https://www.baeldung.com/hibernate-one-to-many">Hibernate One to Many Annotation Tutorial</a><br>
 * <a href="https://www.baeldung.com/hibernate-dynamic-mapping">Dynamic Mapping with Hibernate</a><br>
 * <a href="https://www.baeldung.com/hibernate-many-to-many">Hibernate Many to Many Annotation Tutorial</a><br>
 * </p>
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "cloud_files", uniqueConstraints = {
        @UniqueConstraint(name = "ix_cloud_files_user_id_file_name", columnNames = {"user_id", "file_name"})
})
public class AppCloudFile implements CloudFile {
    @Id
    private UUID id;

    @Column(nullable = false, length = 1000)
    private String fileName;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @Embedded
    private AppCloudUser user;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "cloudFile")
    private AppStorageFile storageFile;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "cloudFile")
    private StatusLatest statusLatest;

    public AppCloudFile(CloudFile cloudFile) {
        this.id = cloudFile.getId();
        this.fileName = cloudFile.getFileName();
        this.createdAt = cloudFile.getCreatedAt();
        this.updatedAt = cloudFile.getUpdatedAt();
        setUser(cloudFile.getUser());
        setStatus(cloudFile.getStatus());
        setStorageFile(cloudFile.getStorageFile());
    }

    @Override
    public CloudFileStatus getStatus() {
        return this.statusLatest.getStatus();
    }

    private void setStatus(CloudFileStatus cloudFileStatus) {
        this.statusLatest = new StatusLatest(this, new AppCloudFileStatus(this, cloudFileStatus));
    }

    private void setUser(CloudUser cloudUser) {
        this.user = new AppCloudUser(cloudUser);
    }

    private void setStorageFile(StorageFile storageFile) {
        if (storageFile == null) {
            return;
        }
        this.storageFile = new AppStorageFile(this, storageFile);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Embeddable
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "user_id", nullable = false))
    })
    public static class AppCloudUser implements CloudUser {
        public UUID id;

        public AppCloudUser(CloudUser cloudUser) {
            this.id = cloudUser.getId();
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Entity
    @Table(name = "cloud_file_status_latest")
    public static class StatusLatest {
        @Id
        private UUID cloudFileId;

        @OneToOne
        @PrimaryKeyJoinColumn
        private AppCloudFile cloudFile;

        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "cloud_file_status_id", nullable = false)
        private AppCloudFileStatus status;

        public StatusLatest(AppCloudFile cloudFile, AppCloudFileStatus status) {
            this.cloudFileId = cloudFile.getId();
            this.cloudFile = cloudFile;
            this.status = status;
        }
    }
}
