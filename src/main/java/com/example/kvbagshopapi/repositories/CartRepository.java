package com.example.kvbagshopapi.repositories;

import com.example.kvbagshopapi.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findAllByUserId(Long UserId);
    int countByUserId(Long UserId);
}
