package ru.netology.cloudstorage.webapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;
import ru.netology.cloudstorage.contracts.core.model.CloudUser;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "users")
public class AppUser implements CloudUser, UserDetails, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Size(max = 100)
    @NotNull
    @Column(name = "username", nullable = false, length = 100)
    private String username;

    @Size(max = 500)
    @NotNull
    @Column(name = "password", nullable = false, length = 500)
    private String password;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = false;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "username", referencedColumnName = "username")
    private Set<AppAuthority> authorities = new LinkedHashSet<>();

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getName()).append(" [");
        sb.append("Id=").append(id).append(", ");
        sb.append("Username=").append(username).append(", ");
        sb.append("Password=[PROTECTED], ");
        sb.append("Enabled=").append(enabled).append(", ");
        sb.append("AccountNonExpired=").append(isAccountNonExpired()).append(", ");
        sb.append("CredentialsNonExpired=").append(isCredentialsNonExpired()).append(", ");
        sb.append("AccountNonLocked=").append(isAccountNonLocked()).append(", ");
        sb.append("Granted Authorities=").append(getAuthorities()).append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppUser appUser)) return false;
        return Objects.equals(getId(), appUser.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
