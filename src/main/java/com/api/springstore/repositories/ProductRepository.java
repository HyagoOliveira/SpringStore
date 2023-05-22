package com.api.springstore.repositories;

import com.api.springstore.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByNameContaining(String name);
}
