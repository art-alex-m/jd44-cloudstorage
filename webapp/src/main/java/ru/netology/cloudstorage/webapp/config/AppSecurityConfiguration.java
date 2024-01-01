package ru.netology.cloudstorage.webapp.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import ru.netology.cloudstorage.webapp.model.AppAuthTokenProperties;
import ru.netology.cloudstorage.webapp.service.AppAuthHeaderAuthenticationProvider;

/**
 * Security configuration
 *
 * <p>
 * <a href="https://bwgjoseph.com/spring-security-custom-pre-authentication-flow">Spring Security: Custom Pre-Authentication Flow</a><br>
 * <a href="https://bwgjoseph.com/why-requestheaderauthenticationfilter-is-not-registered-as-part-of-spring-security-filter-chain">Why RequestHeaderAuthenticationFilter is not registered as part of Spring Security Filter Chain</a><br>
 * <a href="https://shzhangji.com/blog/2023/01/15/restful-api-authentication-with-spring-security/">RESTful API Authentication with Spring Security</a><br>
 * <a href="https://www.baeldung.com/spring-security-jdbc-authentication">Spring Security: Exploring JDBC Authentication</a><br>
 * </p>
 */
@Profile("security-enable")
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class AppSecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain baseSecurityFilter(HttpSecurity httpSecurity,
            AuthenticationManager authenticationManager,
            RequestHeaderAuthenticationFilter authTokenAuthenticationFilter) throws Exception {

        return httpSecurity
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .rememberMe(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationManager(authenticationManager)
                .addFilter(authTokenAuthenticationFilter)
                .authorizeHttpRequests(c -> c.requestMatchers("/login/**", "/logout/**").permitAll())
                .authorizeHttpRequests(c -> c.anyRequest().authenticated())
                .build();
    }

    @Bean
    public RequestHeaderAuthenticationFilter authTokenAuthenticationFilter(AuthenticationManager authenticationManager,
            AppAuthTokenProperties tokenProperties) {
        RequestHeaderAuthenticationFilter filter = new RequestHeaderAuthenticationFilter();
        filter.setPrincipalRequestHeader(tokenProperties.getHeaderName());
        filter.setExceptionIfHeaderMissing(false);
        filter.setAuthenticationManager(authenticationManager);

        return filter;
    }

    @Bean
    public FilterRegistrationBean<RequestHeaderAuthenticationFilter> excludeFromServletAuthTokenAuthenticationFilter(
            RequestHeaderAuthenticationFilter authTokenAuthenticationFilter) {
        FilterRegistrationBean<RequestHeaderAuthenticationFilter> registration =
                new FilterRegistrationBean<>(authTokenAuthenticationFilter);
        registration.setEnabled(false);

        return registration;
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider(UserDetailsService appUserDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(appUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity,
            AppAuthHeaderAuthenticationProvider authHeaderAuthenticationProvider,
            AuthenticationProvider daoAuthenticationProvider) throws Exception {

        AuthenticationManagerBuilder builder = httpSecurity
                .getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authHeaderAuthenticationProvider)
                .authenticationProvider(daoAuthenticationProvider);

        return builder.build();
    }
}
