package com.api.springstore.preloads;

import com.api.springstore.models.User;
import com.api.springstore.repositories.UserRepository;
import com.api.springstore.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class UserPreloader {
    @Bean
    public CommandLineRunner initializeUsers(UserRepository repository, UserService service) {
        return args -> {
            //TODO add users data inside files.

            final String adminUsername = "hyagogow";
            final String simpleUsername = "fulano";

            var hasAdminUser = repository.countByUsername(adminUsername) > 0;
            var hasSimpleUser = repository.countByUsername(simpleUsername) > 0;

            if (!hasSimpleUser)
                service.save("Fulano de Tal", simpleUsername, "fulano123", User.USER);

            if (!hasAdminUser)
                service.save("Hyago Oliveira", adminUsername, "hyago123", User.ADMIN, User.USER);
        };
    }
}
