package com.api.springstore.services;

import com.api.springstore.models.User;
import com.api.springstore.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.NoSuchElementException;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService service;
    @Mock
    private UserRepository repository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void loadUserByUsername_ReturnsUser() {
        var expected = getUser();
        var username = expected.getUsername();
        BDDMockito.
                when(repository.findByUsername(username)).
                thenReturn(expected);

        var actual = service.loadUserByUsername(username);

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void loadUserByUsername_ThrowsNoSuchElementException_WhenNotFound() {
        var invalidUsername = "invalid";
        BDDMockito.
                when(repository.findByUsername(invalidUsername)).
                thenReturn(null);

        Assertions
                .assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> service.loadUserByUsername(invalidUsername));
    }

    @Test
    void save_SavesUser() {
        var expected = getUser();
        var authorities = new String[]{"TestRole00", "TestRole01"};

        BDDMockito.
                when(repository.save(ArgumentMatchers.any(User.class))).
                thenReturn(expected);

        var actual = service.save(expected.getName(), expected.getUsername(), expected.getPassword(), authorities);

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    public static User getUser() {
        return User.builder()
                .id(UUID.randomUUID())
                .name("Test User")
                .username("Test Username")
                .password("Test Password")
                .authorities("Test Authorities")
                .enabled(true)
                .build();
    }
}