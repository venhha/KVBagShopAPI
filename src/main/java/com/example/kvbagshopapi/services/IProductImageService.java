package com.example.kvbagshopapi.services;

import com.example.kvbagshopapi.dtos.ProductImageDto;

import java.util.List;

public interface IProductImageService {
    List<ProductImageDto> getAllImageByProductId(Long productId);

    ProductImageDto addProductImage(ProductImageDto productImageDto, Long productId);

    void deleteProductImage(Long pImage);
}
