package ru.netology.cloudstorage.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync(proxyTargetClass = true)
public class CloudstorageWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudstorageWebApplication.class, args);
    }
}
