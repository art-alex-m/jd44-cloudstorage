package ru.netology.cloudstorage.webapp.boundary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Value;

@Value
public class AppLoginRequest {
    @NotEmpty
    @Email
    String login;

    @JsonProperty(access = Access.WRITE_ONLY)
    @NotEmpty
    String password;
}
