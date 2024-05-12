package ru.netology.cloudstorage.contracts.auth.model;

import java.util.List;

public final class PermissionFiles {
    public static final String CREATE = "permission:file:create";
    public static final String DOWNLOAD = "permission:file:download";
    public static final String DELETE = "permission:file:delete";
    public static final String LIST = "permission:file:list";
    public static final String UPDATE = "permission:file:update";

    public static final List<String> USER_ALL = List.of(CREATE, DOWNLOAD, DELETE, LIST, UPDATE);

    private PermissionFiles() {}
}
