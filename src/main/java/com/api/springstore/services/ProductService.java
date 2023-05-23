package com.api.springstore.services;

import com.api.springstore.dtos.ProductDto;
import com.api.springstore.models.Product;
import com.api.springstore.repositories.ProductRepository;
import com.api.springstore.services.utils.LocalDateTimeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductRepository repository;
    private final LocalDateTimeService localDateTime;

    public ProductService(ProductRepository repository, LocalDateTimeService localDateTime) {
        this.repository = repository;
        this.localDateTime = localDateTime;
    }

    public Product findById(UUID id) {
        return repository.findById(id)
                .orElseThrow();
    }

    public Page<Product> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<Product> findByName(String name) {
        return repository.findByNameContaining(name);
    }

    @Transactional
    public Product save(ProductDto dto) {
        var product = dto.toProduct();
        product.setRegistration(localDateTime.getCurrentDateTimeInUTCZone());

        return repository.save(product);
    }

    public Product update(UUID id, ProductDto dto) {
        var product = findById(id);

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setPictureUrl(dto.getPictureUrl());
        product.setDescription(dto.getDescription());

        return repository.save(product);
    }
}
