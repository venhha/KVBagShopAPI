package com.example.kvbagshopapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private long id;
    private String productName;
    private int quantity;
    private int sold;
    private String description;
    private int price;
    private Boolean isActive = true;
    private CategoryDto category;
    private List<ProductImageDto> productImages;
}
