package ru.netology.cloudstorage.webapp.config;

import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import ru.netology.cloudstorage.contracts.auth.model.PermissionFiles;
import ru.netology.cloudstorage.webapp.factory.AuthenticationTestFactory;

@Profile("security-disable")
@TestConfiguration
@EnableWebSecurity
public class SecurityDisabledConfiguration {

    @Autowired
    private AuthenticationTestFactory authenticationTestFactory;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain permitAllRequests(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .rememberMe(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(a -> a.anyRequest().permitAll())
                .build();
    }

    @Bean
    public UserDetailsService inMemoryUserDetailsManager() {
        UserDetails testUser = User.withUsername(authenticationTestFactory.getTestUsername())
                .password("{noop}" + authenticationTestFactory.getTestUserPassword())
                .authorities(PermissionFiles.USER_ALL.stream().map(SimpleGrantedAuthority::new).toList())
                .build();

        return new InMemoryUserDetailsManager(testUser);
    }
}
