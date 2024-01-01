package ru.netology.cloudstorage.contracts.auth.model;

import java.util.List;

public interface PermissionFiles {
    String CREATE = "permission:file:create";
    String DOWNLOAD = "permission:file:download";
    String DELETE = "permission:file:delete";
    String LIST = "permission:file:list";
    String UPDATE = "permission:file:update";

    List<String> USER_ALL = List.of(CREATE, DOWNLOAD, DELETE, LIST, UPDATE);
}
