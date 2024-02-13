package ru.netology.cloudstorage.webapp.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatus;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatusCode;
import ru.netology.cloudstorage.contracts.core.model.TraceId;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cloud_file_statuses")
public class AppCloudFileStatus implements CloudFileStatus {
    @Id
    private UUID id;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private CloudFileStatusCode code;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "status", orphanRemoval = true)
    private StatusMessage statusMessage;

    @NotNull
    @Embedded
    private StatusTraceId traceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cloud_file_id", referencedColumnName = "id", nullable = false)
    private AppCloudFile cloudFile;

    @Builder
    public AppCloudFileStatus(AppCloudFile cloudFile, UUID id, CloudFileStatusCode code, String message,
            TraceId traceId, Instant createdAt, Instant updatedAt) {
        this.cloudFile = cloudFile;
        this.id = id;
        this.code = code;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        setMessage(message);
        setTraceId(traceId);
    }

    public AppCloudFileStatus(AppCloudFile cloudFile, CloudFileStatus status) {
        this(cloudFile, status.getId(), status.getCode(), status.getMessage(), status.getTraceId(),
                status.getCreatedAt(), status.getUpdatedAt());
    }

    @Override
    public String getMessage() {
        return statusMessage != null ? statusMessage.getMessage() : null;
    }

    private void setMessage(String message) {
        if (message == null) {
            return;
        }
        statusMessage = new StatusMessage(id, message, this);
    }

    private void setTraceId(TraceId traceId) {
        this.traceId = new StatusTraceId(traceId.getUuid(), traceId.getId());
    }

    @Entity
    @Table(name = "cloud_file_status_messages")
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StatusMessage {
        @Id
        private UUID id;

        @Column(name = "message", nullable = false, length = 1000)
        @Length(min = 1)
        private String message;

        @OneToOne
        @PrimaryKeyJoinColumn
        private AppCloudFileStatus status;
    }

    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @AttributeOverrides({
            @AttributeOverride(name = "uuid", column = @Column(name = "trace_id_uuid", nullable = false)),
            @AttributeOverride(name = "id", column = @Column(name = "trace_id", nullable = false))
    })
    public static class StatusTraceId implements TraceId {
        private UUID uuid;

        private long id;
    }
}
