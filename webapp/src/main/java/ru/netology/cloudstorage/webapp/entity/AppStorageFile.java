package ru.netology.cloudstorage.webapp.entity;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @MapsId
    @OneToOne
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
