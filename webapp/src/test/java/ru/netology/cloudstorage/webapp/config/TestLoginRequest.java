package ru.netology.cloudstorage.webapp.config;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Value;

@Value
public class TestLoginRequest {
    @NotEmpty
    @Email
    String login;

    @NotEmpty
    String password;
}
