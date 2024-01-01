package ru.netology.cloudstorage.webapp.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.netology.cloudstorage.webapp.model.AppAuthTokenProperties;

@Configuration
@EnableWebMvc
@EnableConfigurationProperties(AppAuthTokenProperties.class)
public class AppBaseConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedOriginPatterns("http://localhost:*")
                .allowedMethods("*");
    }
}
