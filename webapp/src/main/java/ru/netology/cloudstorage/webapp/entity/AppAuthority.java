package ru.netology.cloudstorage.webapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "authorities")
public class AppAuthority implements GrantedAuthority {
    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private PK primaryKey;

    public String getUsername() {
        return primaryKey.getUsername();
    }

    @Override
    public String getAuthority() {
        return primaryKey.getAuthority();
    }

    @Override
    public String toString() {
        return getAuthority();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppAuthority that)) return false;
        return Objects.equals(getPrimaryKey(), that.getPrimaryKey());
    }

    @Override
    public int hashCode() {
        return getPrimaryKey().hashCode();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PK implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        @Size(max = 100)
        @NotNull
        @Column(name = "username", nullable = false, length = 100)
        private String username;

        @Size(max = 250)
        @NotNull
        @Column(name = "authority", nullable = false, length = 250)
        private String authority;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PK pk)) return false;
            return Objects.equals(getUsername(), pk.getUsername()) && Objects.equals(getAuthority(),
                    pk.getAuthority());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getUsername(), getAuthority());
        }
    }
}
