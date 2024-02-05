package ru.netology.cloudstorage.webapp.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.CloudUser;
import ru.netology.cloudstorage.contracts.db.repository.ListCloudFileInputDbRepository;

import java.util.List;

@RequiredArgsConstructor
@Component("appListCloudFileInputDbRepository")
public class AppListCloudFileInputDbRepository implements ListCloudFileInputDbRepository {

    private final AppCloudFileCrudRepository jpaRepository;

    @Override
    public List<CloudFile> findByUserAndReadyStatus(CloudUser user, int limit) {
        return jpaRepository.findByUserIdAndReadyStatusAndLimit(user.getId(), PageRequest.of(0, limit));
    }
}
