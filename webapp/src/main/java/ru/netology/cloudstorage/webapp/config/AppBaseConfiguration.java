package ru.netology.cloudstorage.webapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.netology.cloudstorage.contracts.core.factory.TraceIdFactory;
import ru.netology.cloudstorage.contracts.core.model.TraceId;
import ru.netology.cloudstorage.contracts.core.model.TraceIdContainer;
import ru.netology.cloudstorage.core.factory.CoreTraceIdFactory;
import ru.netology.cloudstorage.core.model.CoreTraceIdContainer;
import ru.netology.cloudstorage.webapp.model.AppAuthTokenProperties;
import ru.netology.cloudstorage.webapp.service.AppTraceIdInterceptor;

@Configuration
@EnableWebMvc
@EnableConfigurationProperties(AppAuthTokenProperties.class)
public class AppBaseConfiguration implements WebMvcConfigurer {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedOriginPatterns("http://localhost:*")
                .allowedMethods("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(applicationContext.getBean(AppTraceIdInterceptor.class));
    }

    @Bean
    public TraceIdFactory appTraceIdFactory() {
        return new CoreTraceIdFactory();
    }

    @Bean
    @RequestScope
    public TraceIdContainer requestTraceIdContainer(TraceIdFactory appTraceIdFactory) {
        return new CoreTraceIdContainer(appTraceIdFactory.create());
    }

    @Bean
    @RequestScope
    public TraceId requestTraceId(TraceIdContainer requestTraceIdContainer) {
        return requestTraceIdContainer.getTraceId();
    }
}
