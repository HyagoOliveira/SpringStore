package com.api.springstore.controllers;

import com.api.springstore.assemblers.ProductAssembler;
import com.api.springstore.dtos.ProductDto;
import com.api.springstore.models.Product;
import com.api.springstore.services.ProductService;
import com.api.springstore.services.ProductServiceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
class ProductControllerTest {
    @InjectMocks
    private ProductController controller;
    @Mock
    private ProductService service;
    @Mock
    private ProductAssembler assembler;

    @Test
    void findById_ReturnsProduct() {
        var expected = ProductServiceTest.getProduct();
        BDDMockito.
                when(service.findById(expected.getId())).
                thenReturn(expected);
        BDDMockito.
                when(assembler.toModel(expected)).
                thenReturn(EntityModel.of(expected));

        var request = controller.findById(expected.getId());
        var body = request.getBody();
        var actual = body.getContent();

        Assertions.assertThat(request).isNotNull();
        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(request.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void list_ReturnsProductList() {
        var expected = EntityModel.of(ProductServiceTest.getProduct());
        var expectedModel = CollectionModel.of(List.of(expected));
        BDDMockito.
                when(assembler.toModelList(any(), any())).
                thenReturn(expectedModel);

        var request = controller.list(null);
        CollectionModel<EntityModel<Product>> body = request.getBody();

        Assertions.assertThat(request).isNotNull();
        Assertions.assertThat(request.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.getContent()).isNotEmpty().hasSize(1);
        Assertions.assertThat(body.getContent().toArray()[0]).isEqualTo(expected);
    }

    @Test
    void list_ReturnsEmptyProductList_WhenNotFound() {
        CollectionModel<EntityModel<Product>> expectedModel =
                CollectionModel.of(CollectionModel.empty());
        BDDMockito.
                when(assembler.toModelList(any(), any())).
                thenReturn(expectedModel);

        var request = controller.list(null);
        CollectionModel<EntityModel<Product>> body = request.getBody();

        Assertions.assertThat(request).isNotNull();
        Assertions.assertThat(request.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.getContent()).isEmpty();
    }

    @Test
    void findByName_ReturnsProductList() {
        var expected = EntityModel.of(ProductServiceTest.getProduct());
        var expectedModel = CollectionModel.of(List.of(expected));
        BDDMockito.
                when(assembler.toModelListByName(any(), any())).
                thenReturn(expectedModel);

        var request = controller.findByName(null);
        CollectionModel<EntityModel<Product>> body = request.getBody();

        Assertions.assertThat(request).isNotNull();
        Assertions.assertThat(request.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.getContent()).isNotEmpty().hasSize(1);
        Assertions.assertThat(body.getContent().toArray()[0]).isEqualTo(expected);
    }

    @Test
    void findByName_ReturnsEmptyProductList_WhenNotFound() {
        CollectionModel<EntityModel<Product>> expectedModel =
                CollectionModel.of(CollectionModel.empty());
        BDDMockito.
                when(assembler.toModelListByName(any(), any())).
                thenReturn(expectedModel);

        var request = controller.findByName(null);
        CollectionModel<EntityModel<Product>> body = request.getBody();

        Assertions.assertThat(request).isNotNull();
        Assertions.assertThat(request.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.getContent()).isEmpty();
    }

    @Test
    void save_ReturnsProduct() {
        var expected = ProductServiceTest.getProduct();
        var dtoParam = ProductDto.fromProduct(expected);
        BDDMockito.
                when(service.save(dtoParam)).
                thenReturn(expected);

        var request = controller.save(dtoParam);
        var actual = request.getBody();

        Assertions.assertThat(request).isNotNull();
        Assertions.assertThat(request.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void update_ReturnsProduct() {
        var expected = ProductServiceTest.getProduct();
        var idParam = expected.getId();
        var dtoParam = ProductDto.fromProduct(expected);
        BDDMockito.
                when(service.update(idParam, dtoParam)).
                thenReturn(expected);

        var request = controller.update(idParam, dtoParam);
        var actual = request.getBody();

        Assertions.assertThat(request).isNotNull();
        Assertions.assertThat(request.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void remove_RemovesProduct() {
        var idParam = UUID.randomUUID();
        BDDMockito.
                doNothing().
                when(service).
                delete(idParam);

        var entity = controller.remove(idParam);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}