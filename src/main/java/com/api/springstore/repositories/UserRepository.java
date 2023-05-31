package com.api.springstore.repositories;

import com.api.springstore.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    long countByUsername(String username);
}
