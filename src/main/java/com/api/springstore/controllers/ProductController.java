package com.api.springstore.controllers;

import com.api.springstore.dtos.ProductDto;
import com.api.springstore.models.Product;
import com.api.springstore.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Product> save(@RequestBody @Valid ProductDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }
}
