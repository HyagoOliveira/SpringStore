package com.api.springstore.services;

import com.api.springstore.models.User;
import com.api.springstore.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws NoSuchElementException {
        var user = repository.findByUsername(username);
        return Optional.ofNullable(user).orElseThrow();
    }

    @Transactional
    public User save(String name, String username, String password, String... roles) {
        var encodedPassword = passwordEncoder.encode(password);
        var authorities = String.join(User.AUTH_SPLIT, roles);

        var user = User.builder()
                .name(name)
                .username(username)
                .password(encodedPassword)
                .enabled(true)
                .authorities(authorities)
                .build();

        return repository.save(user);
    }
}
