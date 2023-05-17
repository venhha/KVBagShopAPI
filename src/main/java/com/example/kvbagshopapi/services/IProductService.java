package com.example.kvbagshopapi.services;

import com.example.kvbagshopapi.dtos.ProductDto;
import com.example.kvbagshopapi.dtos.ProductPagination;
import com.example.kvbagshopapi.entities.Category;

import java.util.List;
import java.util.Map;

public interface IProductService {


    List<ProductDto> getAllProducts();

    ProductPagination getAllPagingProducts(int pageNo, int pageSize, String sortBy, String sortDir);

    ProductDto getProductById(Long ProductId);

    ProductDto createProduct(ProductDto ProductDto);

    // Cập nhật lại Product (chỉ cập nhật những thuộc tính muốn thay đổi)
    ProductDto patchProduct(Long id, Map<Object, Object> ProductDto);

    // Cập nhật lại Product (Cập nhật lại toàn bộ các thuộc tính)
    ProductDto updateProduct(Long id, ProductDto productDto);

    void deleteProduct(Long ProductId);

    List<ProductDto> searchByProductName(String name);

    List<ProductDto> searchByProductNameAndCategory(String name, Category category);

    List<ProductDto> getTop10ProductBySold();
    List<ProductDto> getProductByCategoryId(long id);

    List<ProductDto> getTop10NewProducts();
}
