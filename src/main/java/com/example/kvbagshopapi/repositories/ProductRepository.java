package com.example.kvbagshopapi.repositories;

import com.example.kvbagshopapi.entities.Category;
import com.example.kvbagshopapi.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByProductNameContainingIgnoreCase(String name);
    List<Product> findByProductNameContainingAndCategoryAllIgnoreCase(String productName, Category category);
    List<Product> findTop10ByOrderBySoldDesc();
    List<Product> findTop10ByOrderByCreateAtDesc();

    List<Product> findByCategoryId(long category);
}
