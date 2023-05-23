package com.api.springstore.controllers;

import com.api.springstore.assemblers.ProductAssembler;
import com.api.springstore.dtos.ProductDto;
import com.api.springstore.models.Product;
import com.api.springstore.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("products")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductController {
    private final ProductService service;
    private final ProductAssembler assembler;

    public ProductController(ProductService service, ProductAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping(path = "/{id}")
    public EntityModel<Product> findById(@PathVariable UUID id) {
        var product = service.findById(id);
        return assembler.toModel(product);
    }

    @GetMapping
    public CollectionModel<EntityModel<Product>> list(Pageable pageable) {
        var products = service.list(pageable);
        return assembler.toModelList(products, pageable);
    }

    @GetMapping(path = "/find")
    public ResponseEntity<List<Product>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(service.findByName(name));
    }

    @PostMapping
    public ResponseEntity<Product> save(@RequestBody @Valid ProductDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(
            @PathVariable UUID id,
            @RequestBody @Valid ProductDto dto
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable UUID id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
