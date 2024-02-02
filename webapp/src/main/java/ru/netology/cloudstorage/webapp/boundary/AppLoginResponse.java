package ru.netology.cloudstorage.webapp.boundary;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class AppLoginResponse {

    @JsonProperty("auth-token")
    String authToken;
}
