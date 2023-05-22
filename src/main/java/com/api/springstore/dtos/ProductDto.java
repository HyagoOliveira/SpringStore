package com.api.springstore.dtos;

import com.api.springstore.models.Product;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class ProductDto {
    @NotBlank
    private String name;
    @URL
    private String pictureUrl;
    private String description;
    @DecimalMin("0.0")
    private double price;

    public Product toProduct(){
        return Product.builder()
                .name(getName())
                .pictureUrl(getPictureUrl())
                .description(getDescription())
                .price(getPrice())
                .build();
    }
}
