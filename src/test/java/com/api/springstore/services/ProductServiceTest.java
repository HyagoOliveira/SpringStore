package com.api.springstore.services;

import com.api.springstore.dtos.ProductDto;
import com.api.springstore.exceptions.BadRequestException;
import com.api.springstore.models.Product;
import com.api.springstore.repositories.ProductRepository;
import com.api.springstore.services.utils.LocalDateTimeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {
    @InjectMocks
    private ProductService service;
    @Mock
    private ProductRepository repository;
    @Mock
    private LocalDateTimeService localDateTime;

    @Test
    void findById_ReturnsProduct() {
        var expected = getProduct();
        BDDMockito.
                when(repository.findById(expected.getId())).
                thenReturn(Optional.of(expected));

        var actual = service.findById(expected.getId());

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findById_ThrowsBadRequestException_WhenIdIsNotFound() {
        var invalidId = UUID.randomUUID();
        BDDMockito.
                when(repository.findById(invalidId)).
                thenReturn(Optional.empty());

        Assertions
                .assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> service.findById(invalidId));
    }

    @Test
    void list_ReturnsProductList() {
        var expected = getProduct();
        BDDMockito.
                when(repository.findAll(ArgumentMatchers.any(Pageable.class))).
                thenReturn(new PageImpl<>(List.of(expected)));

        Pageable pageable = mock(Pageable.class);
        Page<Product> actual = service.list(pageable);

        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.toList()).isNotEmpty().hasSize(1);
        Assertions.assertThat(actual.toList().get(0)).isEqualTo(expected);
    }

    @Test
    void list_ReturnsEmptyProductList_WhenNotFound() {
        BDDMockito.
                when(repository.findAll(ArgumentMatchers.any(Pageable.class))).
                thenReturn(new PageImpl<>(Collections.emptyList()));

        Pageable pageable = mock(Pageable.class);
        Page<Product> actual = service.list(pageable);

        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.toList()).isNotNull().isEmpty();
    }

    @Test
    void findByName_ReturnsProductList() {
        var expected = getProduct();
        BDDMockito.
                when(repository.findByNameContaining(expected.getName())).
                thenReturn(List.of(expected));

        List<Product> actual = service.findByName(expected.getName());

        Assertions.assertThat(actual).isNotNull().hasSize(1);
        Assertions.assertThat(actual.get(0)).isEqualTo(expected);
    }

    @Test
    void findByName_ReturnsEmptyProductList_WhenNotFound() {
        var invalidName = "";
        BDDMockito.
                when(repository.findByNameContaining(invalidName)).
                thenReturn(Collections.emptyList());

        List<Product> actual = service.findByName(invalidName);

        Assertions.assertThat(actual).isNotNull().isEmpty();
    }

    @Test
    void save_ReturnsProduct() {
        var expected = getProduct();
        var dto = ProductDto.fromProduct(expected);
        BDDMockito.
                when(repository.save(ArgumentMatchers.any(Product.class))).
                thenReturn(expected);

        Product actual = service.save(dto);

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void save_UsesCurrentDateTimeFromUTCZone_AsRegistration() {
        var expected = getProduct();
        var expectedRegistration = LocalDateTime.now();
        var dto = ProductDto.fromProduct(expected);
        BDDMockito.
                when(repository.save(ArgumentMatchers.any(Product.class))).
                thenReturn(expected);
        BDDMockito.
                when(localDateTime.getCurrentDateTimeInUTCZone()).
                thenReturn(expectedRegistration);

        Product actual = service.save(dto);

        Assertions.assertThat(actual.getRegistration()).isEqualTo(expectedRegistration);
    }

    @Test
    void update_ReturnsProduct() {
        var expected = getProduct();
        var expectedId = UUID.randomUUID();
        var dto = ProductDto.fromProduct(expected);
        BDDMockito.
                when(repository.findById(expectedId)).
                thenReturn(Optional.of(expected));
        BDDMockito.
                when(repository.save(ArgumentMatchers.any(Product.class))).
                thenReturn(expected);

        Product actual = service.update(expectedId, dto);

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void update_ThrowsBadRequestException_WhenIdIsNotFound() {
        var expected = getProduct();
        var invalidId = UUID.randomUUID();
        var dto = ProductDto.fromProduct(expected);
        BDDMockito.
                when(repository.findById(invalidId)).
                thenReturn(Optional.empty());

        Assertions
                .assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> service.update(invalidId, dto));
    }

    @Test
    void delete_ThrowsBadRequestException_WhenIdIsNotFound() {
        var invalidId = UUID.randomUUID();
        BDDMockito.
                when(repository.findById(invalidId)).
                thenReturn(Optional.empty());

        Assertions
                .assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> service.delete(invalidId));
    }

    public static Product getProduct() {
        return Product.builder()
                .id(UUID.randomUUID())
                .name("Test Product")
                .pictureUrl("https://some.domain/picture.png")
                .description("This is a product for testing.")
                .price(1.0D)
                .registration(LocalDateTime.now())
                .build();
    }
}