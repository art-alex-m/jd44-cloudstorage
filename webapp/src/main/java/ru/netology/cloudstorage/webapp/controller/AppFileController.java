package ru.netology.cloudstorage.webapp.controller;


import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.cloudstorage.webapp.input.AppSaveFileInputRequest;

@RestController
public class AppFileController {

    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String createCloudFile(@Validated AppSaveFileInputRequest request) {
        System.out.println(request);
        return "Ok";
    }
}
