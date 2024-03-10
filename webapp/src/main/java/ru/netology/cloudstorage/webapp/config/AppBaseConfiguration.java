package ru.netology.cloudstorage.webapp.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.netology.cloudstorage.contracts.core.model.TraceId;
import ru.netology.cloudstorage.contracts.trace.factory.TraceIdFactory;
import ru.netology.cloudstorage.contracts.trace.model.TraceIdContainer;
import ru.netology.cloudstorage.contracts.trace.model.TraceIdHeader;
import ru.netology.cloudstorage.core.factory.CoreTraceIdFactory;
import ru.netology.cloudstorage.core.model.CoreTraceIdContainer;
import ru.netology.cloudstorage.webapp.model.AppAuthTokenProperties;

@Configuration
@EnableWebMvc
@EnableConfigurationProperties(AppAuthTokenProperties.class)
@EnableAspectJAutoProxy
public class AppBaseConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedOriginPatterns("http://localhost:*")
                .allowedMethods("*");
    }

    @Bean
    public TraceIdFactory appTraceIdFactory() {
        return new CoreTraceIdFactory();
    }

    /**
     * Устанавливает TraceId из атрибута запроса.
     * Атрибут запроса устанавливает AppTraceIdFilter
     *
     * @see ru.netology.cloudstorage.webapp.service.AppTraceIdFilter
     */
    @Bean
    @RequestScope
    public TraceIdContainer requestTraceIdContainer(HttpServletRequest httpServletRequest) {
        return new CoreTraceIdContainer((TraceId) httpServletRequest.getAttribute(TraceIdHeader.TRACE_ID));
    }

    @Bean
    @RequestScope
    public TraceId requestTraceId(TraceIdContainer requestTraceIdContainer) {
        return requestTraceIdContainer.getTraceId();
    }
}
