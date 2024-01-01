package ru.netology.cloudstorage.webapp.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Value;

@Value
public class AppLoginRequest {
    @NotEmpty
    @Email
    String login;

    @NotEmpty
    String password;
}
