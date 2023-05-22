package com.api.springstore.services;

import com.api.springstore.dtos.ProductDto;
import com.api.springstore.models.Product;
import com.api.springstore.repositories.ProductRepository;
import com.api.springstore.services.utils.LocalDateTimeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository repository;
    private final LocalDateTimeService localDateTime;

    public ProductService(ProductRepository repository, LocalDateTimeService localDateTime) {
        this.repository = repository;
        this.localDateTime = localDateTime;
    }

    @Transactional
    public Product save(ProductDto dto) {
        var product = dto.toProduct();
        product.setRegistration(localDateTime.getCurrentDateTimeInUTCZone());

        return repository.save(product);
    }
}
