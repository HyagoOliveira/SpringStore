package com.api.springstore.dtos;

import com.api.springstore.models.Product;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
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

    public static ProductDto fromProduct(Product product){
        return ProductDto.builder()
                .name(product.getName())
                .pictureUrl(product.getPictureUrl())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
