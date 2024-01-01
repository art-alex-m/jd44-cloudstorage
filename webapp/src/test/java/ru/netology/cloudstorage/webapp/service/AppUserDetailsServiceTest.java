package ru.netology.cloudstorage.webapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.netology.cloudstorage.webapp.entity.AppUser;
import ru.netology.cloudstorage.webapp.repository.AppUserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class AppUserDetailsServiceTest {

    @Mock
    private AppUserRepository userRepository;

    @Test
    void loadUserByUsername() {
        String username = "test@example.com";
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(new AppUser()));
        AppUserDetailsService sut = new AppUserDetailsService(userRepository);

        UserDetails result = sut.loadUserByUsername(username);

        assertNotNull(result);
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(username);
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    void loadUserByUsernameNotFound() {
        String username = "test@example.com";
        AppUserDetailsService sut = new AppUserDetailsService(userRepository);

        Executable result = () -> sut.loadUserByUsername(username);

        assertThrows(UsernameNotFoundException.class, result);
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(username);
        Mockito.verifyNoMoreInteractions(userRepository);
    }
}