package ru.netology.cloudstorage.webapp.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.cloudstorage.webapp.input.AppLoginRequest;
import ru.netology.cloudstorage.webapp.input.AppLoginResponse;

@RestController
public class AppAuthController {

    @PostMapping("/login")
    public AppLoginResponse login(@Validated @RequestBody AppLoginRequest request) {
        return new AppLoginResponse("demo-token-value");
    }


    @PostMapping("/logout")
    public void logout() {

    }
}
