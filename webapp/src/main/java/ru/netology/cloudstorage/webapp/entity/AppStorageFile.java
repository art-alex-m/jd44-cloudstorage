package ru.netology.cloudstorage.webapp.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.netology.cloudstorage.contracts.core.model.StorageFile;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "storage_files")
public class AppStorageFile implements StorageFile {
    @Id
    private UUID id;

    @Column(nullable = false)
    private long size;

    @Column(nullable = false, length = 1000)
    private String fileName;

    @Column(nullable = false, length = 100)
    private String mediaType;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cloud_file_id", nullable = false)
    private AppCloudFile cloudFile;

    public AppStorageFile(AppCloudFile cloudFile, StorageFile storageFile) {
        this.cloudFile = cloudFile;
        this.id = storageFile.getId();
        this.size = storageFile.getSize();
        this.fileName = storageFile.getFileName();
        this.mediaType = storageFile.getMediaType();
        this.createdAt = storageFile.getCreatedAt();
        this.updatedAt = storageFile.getUpdatedAt();
    }
}
