package com.example.kvbagshopapi.services;

import com.example.kvbagshopapi.dtos.CartDto;

import java.util.List;

public interface ICartService {
    List<CartDto> getAllCartByUserId(Long userId);


    CartDto getCartById(Long cartId);

    CartDto addCart(CartDto cartDto, Long userId);


    CartDto updateCart(Long cartId, int quantity);

    int countAllCartByUserId(Long userId);

    void deleteCart(Long cartId);
}
