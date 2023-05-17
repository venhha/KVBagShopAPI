package com.example.kvbagshopapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private long id;

    private int quantity;

    private ProductDto product;
}
