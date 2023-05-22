package com.api.springstore.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false, unique = true, length = 120)
    private String name;
    @Column(length = 300)
    private String pictureUrl;
    @Column(length = 1024)
    private String description;
    @Column(nullable = false)
    private double price;
    @Column(nullable = false)
    private LocalDateTime registration;
}
