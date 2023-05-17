package com.example.kvbagshopapi.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    private long id;

    private int quantity;

    private ProductDto product;
}
