package com.api.springstore.assemblers;

import com.api.springstore.controllers.ProductController;
import com.api.springstore.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductAssembler implements RepresentationModelAssembler<Product, EntityModel<Product>> {
    @Override
    public EntityModel<Product> toModel(Product product) {
        return EntityModel.of(
                product,
                linkTo(methodOn(ProductController.class).findById(product.getId())).withSelfRel()
        );
    }

    public CollectionModel<EntityModel<Product>> toModelList(
            Page<Product> pagedProducts,
            Pageable pageable
    ) {
        var products = pagedProducts.stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                products,
                linkTo(methodOn(ProductController.class).list(pageable)).withSelfRel()
        );
    }

    public CollectionModel<EntityModel<Product>> toModelListByName(
            List<Product> productList,
            String name
    ) {
        var products = productList.stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                products,
                linkTo(methodOn(ProductController.class).findByName(name)).withSelfRel()
        );
    }
}
