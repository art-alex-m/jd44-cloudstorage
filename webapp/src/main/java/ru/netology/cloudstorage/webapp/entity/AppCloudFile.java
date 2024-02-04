package ru.netology.cloudstorage.webapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.CloudUser;
import ru.netology.cloudstorage.contracts.core.model.StorageFile;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "cloud_files")
public class AppCloudFile implements CloudFile {
    @Id
    private UUID id;

    @Column(nullable = false, length = 1000)
    private String fileName;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private AppStorageFile storageFile;

    @Embedded
    private AppCloudUser user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(
            name = "cloud_file_status_latest",
            joinColumns = {@JoinColumn(name = "cloud_file_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "cloud_file_status_id", nullable = false)}
    )
    private AppCloudFileStatus status;

    public AppCloudFile(CloudFile cloudFile) {
        this.id = cloudFile.getId();
        this.fileName = cloudFile.getFileName();
        this.createdAt = cloudFile.getCreatedAt();
        this.updatedAt = cloudFile.getUpdatedAt();
        this.user = new AppCloudUser(cloudFile.getUser());
        this.status = new AppCloudFileStatus(this, cloudFile.getStatus());
        setStorageFile(cloudFile.getStorageFile());
    }

    public void setStorageFile(StorageFile storageFile) {
        if (storageFile == null) {
            return;
        }
        this.storageFile = new AppStorageFile(this, storageFile);
        ;
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
}
