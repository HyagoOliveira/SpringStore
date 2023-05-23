package com.api.springstore.preloads;

import com.api.springstore.dtos.ProductDto;
import com.api.springstore.repositories.ProductRepository;
import com.api.springstore.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Configuration
public class ProductPreloader {
    private static final Logger log = LoggerFactory.getLogger(ProductPreloader.class);

    @Bean
    CommandLineRunner initDatabase(ProductRepository repository, ProductService service) {
        return args -> {
            var hasProducts = repository.count() > 0;
            if (hasProducts) return;

            var products = readProductsFromSeedFile();
            for (var product : products) {
                service.save(product);
            }

            log.info("{} Products were preloaded.", products.size());
        };
    }

    private static List<ProductDto> readProductsFromSeedFile() {
        final ObjectMapper mapper = new ObjectMapper();
        final String path = "/preload-data/products.json";
        final ClassPathResource resource = new ClassPathResource(path, ProductPreloader.class);

        try {
            var inputStream = resource.getInputStream();
            var content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            return mapper.readerForListOf(ProductDto.class).readValue(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
}
