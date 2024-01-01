package ru.netology.cloudstorage.webapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.netology.cloudstorage.webapp.entity.AppUser;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppUserRepository extends CrudRepository<AppUser, UUID> {
    Optional<AppUser> findByUsername(String username);
}
